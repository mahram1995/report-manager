package com.mislbd.report_manager.configuration.aopConfig.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mislbd.report_manager.configuration.aopConfig.auditListener.AuditorContextHolder;
import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import com.mislbd.report_manager.configuration.aopConfig.domain.CommandResponse;
import com.mislbd.report_manager.configuration.aopConfig.domain.HasIdentity;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class CommandProcessorImpl implements CommandProcessor {
    @Autowired
    private ObjectMapper objectMapper;
    private final TaskInstanceService taskService;
    private final CommandHandlerAnnotationProcessor commandAnnotationProcessor;
    private final CommandValidatorAnnotationProcessor commandValidator;
    private final CommandService commandService;
    private final CommandListenerProcessor commandListenerProcessor;
    private  final AuditorContextHolder contextHolder;
    @Autowired
    private HttpServletRequest request;

    public CommandProcessorImpl(TaskInstanceService taskService, CommandHandlerAnnotationProcessor commandAnnotationProcessor, CommandValidatorAnnotationProcessor commandValidator, CommandService commandService, CommandListenerProcessor commandListenerProcessor, AuditorContextHolder contextHolder) {
        this.taskService = taskService;
        this.commandAnnotationProcessor = commandAnnotationProcessor;
        this.commandValidator = commandValidator;
        this.commandService = commandService;
        this.commandListenerProcessor = commandListenerProcessor;
        this.contextHolder = contextHolder;
    }

    @SneakyThrows
    @Override
    public Object executeCommand(Object command) {
        String commandName = command.getClass().getSimpleName();

        String domainReference="";

        // Check the is there any task is pending with the same identity
        if (command instanceof HasIdentity hasIdentity) {
             domainReference = hasIdentity.getIdentity();

            if(taskService.existsTaskByDomainReference(commandName,domainReference)){
                TaskInstanceEntity task=taskService.getTaskByDomainRefAndCommandName(commandName,domainReference);
                if(!task.getStatus().contains("CORRECTION")){
                    throw new RuntimeException("Task already exists with same reference : " + domainReference);
                }
            };
        }




        Command  commands = null;
        String payload = "";
        if (command instanceof Command<?> baseCommand) {
            commands=baseCommand;
            payload = objectMapper.writeValueAsString(baseCommand.getPayload());

        }

        // ✅ Get current HTTP request (if exists)
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();

        String detailsUI = request.getHeader("detailsUI");
        String correctionUI = request.getHeader("correctionUI");
        String verifier = request.getHeader("verifier");
        String oldTaskId = request.getHeader("taskId");
        String terminalIp = request.getHeader("terminalIp");
        String initiator = SecurityContextHolder.getContext().getAuthentication().getName();

        contextHolder.setCommand(commands);
        contextHolder.setClientIp(terminalIp);


        commands.setInitiatorTerminal(terminalIp);
        commands.setInitiator(getCurrentUsername());
        commands.setVerifier(verifier);
        commands.setInitiatingTime(LocalDateTime.now());
        commands.setInitiatorClient(getUserAgent());


        Long taskId = null;
        if (oldTaskId != null) {
            taskId = Long.valueOf(oldTaskId);
        }

        // check the right for execute the command
        Boolean isRightToExecuteCommand=isRightToExecuteCommand(commandName);
        if(!isRightToExecuteCommand){
            throw new RuntimeException("You have no right to execute this command");
        }

        // validate the command before execute
        Boolean isCommandValidate=commandValidator.runCommandValidator(command);

        if(isCommandValidate){ // check is the all command attributes are validate. if command is valid then proceed for next.
            CommandEntity commandEntity = commandService.getCommandByCommandName(commandName);
            commands.setApprovalFlowRequired(commandEntity.getIsApprovalFlowRequired());


            if (commandEntity.getIsApprovalFlowRequired()) {

                // call Command Listener to do specific task
                commandListenerProcessor.publishCommandListener(commandName,command, CommandStatus.START.name());

                //save data to task table if approval flow is true
                return saveTaskInstance(commandName, initiator, commands, detailsUI, correctionUI, domainReference,verifier, taskId, terminalIp);
            } else {
                // otherwise execute specific command for do operation
                return commandAnnotationProcessor.runCommand(command);
            }
        }
        return null;
    }

    private Object saveTaskInstance(String commandName, String user, Command command,
                                       String detailsUI, String correctionUI,
                                       String domainReference,
                                       String verifier, Long taskId,
                                      String terminal) {

        TaskInstanceEntity task = new TaskInstanceEntity();
        if (taskId != null) {
            task.setTaskId(taskId);
        }
        task.setMaker(user);
        task.setMakerTerminal(terminal);
        task.setActivityName(toReadableName(commandName));
        task.setCommandName(commandName);
        try {
            task.setPayload(objectMapper.writeValueAsString(command));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        task.setStatus("START");
        task.setTaskDetailsUi(detailsUI);
        task.setDomainReference(domainReference);
        task.setTaskCorrectionUi(correctionUI);
        task.setVerifier(verifier);
        task.setCreateDate(LocalDate.now());
        Long responseTaskId = taskService.saveTaskInstance(task);

        // base on  1001 code in web app, make a response-interceptor to track approval response and show the task id with successful message
        return new CommandResponse<>(Map.of(
                        "status", "success",
                        "code", 1001,
                        "taskId", responseTaskId
                ));
    }

    public static String toReadableName(String className) {
        // Remove package name if any
        String simpleName = className.substring(className.lastIndexOf('.') + 1);
        // Insert space before each capital letter (except the first)
        return simpleName.replaceAll("(?<!^)([A-Z])", " $1").trim();
    }

    public boolean isRightToExecuteCommand(String commandName){
        String userName=getCurrentUsername();
        // need to create a class for  of logic
        return true;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // ✅ username from JWT
        }
        return null;
    }


    private String getUserAgent() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getHeader("User-Agent");
        } catch (Exception e) {
            return "unknown";
        }
    }


}
