package com.mislbd.report_manager.configuration.aopConfig.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mislbd.report_manager.configuration.annotation.Command;
import com.mislbd.report_manager.configuration.aopConfig.entity.TaskInstanceEntity;
import com.mislbd.report_manager.configuration.aopConfig.service.TaskInstanceService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
@Order(3)
public class OperationVerifierAspect {
    private final TaskInstanceService taskService;
    @Autowired
    private ApplicationContext context; //
    @Autowired
    private ObjectMapper objectMapper; // To serialize request body

    @Autowired
    private HttpServletRequest httpServletRequest;

    public OperationVerifierAspect(TaskInstanceService taskService) {
        this.taskService = taskService;
    }

    @Pointcut("@annotation(command)")
    public void verifyPointcut(Command command) {

    }

    @Around("verifyPointcut(command)")
    public Object verifyOperation(ProceedingJoinPoint joinPoint, Command command) throws Throwable {
        String operationName = command.value();
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = getCurrentHttpRequest();
        String detailsUI = request.getHeader("detailsUI");
        String correctionUI = request.getHeader("correctionUI");
        String verifier = request.getHeader("verifier");
        String oldTaskId = request.getHeader("taskId");
        String initiator = SecurityContextHolder.getContext().getAuthentication().getName();
        Long taskId = null;
        if(oldTaskId!=null){
            taskId=Long.valueOf(oldTaskId);
        }

        boolean requiresApproval = checkIfApprovalRequired(operationName);

        if (requiresApproval) {
            // Simulate saving request for approval

            String payload = objectMapper.writeValueAsString(args[0]);

           Long responseTaskId= savePendingApproval(operationName, initiator, payload, detailsUI, correctionUI,verifier, taskId);

            // return fake success response (as if saved and will be verified)
            if(taskId!=null){
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Operation correction sent for\n  verification. Task id is: " +responseTaskId
                ));
            }
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Task sent for verification\n Task id is: " +responseTaskId
            ));
        }

        // No approval required, proceed with original method
        return joinPoint.proceed();
    }

    private HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        throw new IllegalStateException("Failed to get current HTTP request");
    }


    private boolean checkIfApprovalRequired(String operationName) {
        // ✅ You can check from DB or hardcoded list
        return false;
    }

    private Long savePendingApproval(String operation, String user, String payload,
                                     String detailsUI, String correctionUI,
                                     String verifier, Long taskId) {

        TaskInstanceEntity task=new TaskInstanceEntity();
        if(taskId!=null){
            task.setTaskId(taskId);
        }
        task.setMaker(user);
        task.setActivityName(getActivityName(operation));
        task.setCommandName(operation);
        task.setPayload(payload);
        task.setStatus("START");
        task.setTaskDetailsUi(detailsUI);
        task.setTaskCorrectionUi(correctionUI);
        task.setVerifier(verifier);
        task.setCreateDate(LocalDate.now());
        return  taskService.saveTaskInstance(task);
    }

    public String getActivityName(String command) {
        return Arrays.stream(command.split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }





}
