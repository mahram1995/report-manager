package com.mislbd.report_manager.configuration.aopConfig.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import com.mislbd.report_manager.configuration.aopConfig.entity.CommandEntity;
import com.mislbd.report_manager.configuration.aopConfig.entity.TaskInstanceEntity;
import com.mislbd.report_manager.configuration.aopConfig.processor.CommandHandlerAnnotationProcessor;
import com.mislbd.report_manager.configuration.aopConfig.processor.CommandListenerProcessor;
import com.mislbd.report_manager.configuration.aopConfig.processor.CommandValidatorAnnotationProcessor;
import com.mislbd.report_manager.configuration.aopConfig.service.CommandProcessor;
import com.mislbd.report_manager.configuration.aopConfig.service.CommandService;
import com.mislbd.report_manager.configuration.aopConfig.service.TaskInstanceService;
import com.mislbd.report_manager.enam.CommandStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.util.Map;

@Component
public class CommandProcessorImpl implements CommandProcessor {
    @Autowired
    private ObjectMapper objectMapper;
    private final TaskInstanceService taskService;
    private final CommandHandlerAnnotationProcessor commandAnnotationProcessor;
    @Autowired
    private CommandValidatorAnnotationProcessor commandValidator;
    private final CommandService commandService;
    private final CommandListenerProcessor commandListenerProcessor;

    public CommandProcessorImpl(TaskInstanceService taskService, CommandHandlerAnnotationProcessor commandAnnotationProcessor, CommandService commandService, CommandListenerProcessor commandListenerProcessor) {
        this.taskService = taskService;
        this.commandAnnotationProcessor = commandAnnotationProcessor;
        this.commandService = commandService;
        this.commandListenerProcessor = commandListenerProcessor;
    }

    @SneakyThrows
    @Override
    public Object executeCommand(Object command) {
        String payload = "";
        if (command instanceof Command<?> baseCommand) {
            payload = objectMapper.writeValueAsString(baseCommand.getPayload());
        }

        String commandName = command.getClass().getSimpleName();

        // ✅ Get current HTTP request (if exists)
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();

        String detailsUI = request.getHeader("detailsUI");
        String correctionUI = request.getHeader("correctionUI");
        String verifier = request.getHeader("verifier");
        String oldTaskId = request.getHeader("taskId");
        String initiator = SecurityContextHolder.getContext().getAuthentication().getName();
        Long taskId = null;
        if (oldTaskId != null) {
            taskId = Long.valueOf(oldTaskId);
        }
        Boolean isCommandValidate=commandValidator.runCommandValidator(command);
        if(isCommandValidate){ // check is the all command attributes are validate. if command is valid then proceed for next.
            CommandEntity commandEntity = commandService.getCommandByCommandName(commandName);

            if (commandEntity.getIsApprovalFlowRequired()) {
                //save data to task table if approval flow is true
                commandListenerProcessor.publishCommandListener(commandName,payload, CommandStatus.START.name());

                return saveTaskInstance(commandName, initiator, payload, detailsUI, correctionUI, verifier, taskId);
            } else {
                // otherwise execute specific command for do operation
                return commandAnnotationProcessor.runCommand(command);
            }
        }
        return null;
    }

    private Object saveTaskInstance(String command, String user, String payload,
                                       String detailsUI, String correctionUI,
                                       String verifier, Long taskId) {

        TaskInstanceEntity task = new TaskInstanceEntity();
        if (taskId != null) {
            task.setTaskId(taskId);
        }
        task.setMaker(user);
        task.setActivityName(toReadableName(command));
        task.setCommandName(command);
        task.setPayload(payload);
        task.setStatus("START");
        task.setTaskDetailsUi(detailsUI);
        task.setTaskCorrectionUi(correctionUI);
        task.setVerifier(verifier);
        task.setCreateDate(LocalDate.now());
        Long responseTaskId = taskService.saveTaskInstance(task);
        if (taskId != null) {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Operation correction sent for\n  verification. Task id is: " + responseTaskId
            ));
        }
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Task sent for verification\n Task id is: " + responseTaskId
        ));
    }

    public static String toReadableName(String className) {
        // Remove package name if any
        String simpleName = className.substring(className.lastIndexOf('.') + 1);
        // Insert space before each capital letter (except the first)
        return simpleName.replaceAll("(?<!^)([A-Z])", " $1").trim();
    }

}
