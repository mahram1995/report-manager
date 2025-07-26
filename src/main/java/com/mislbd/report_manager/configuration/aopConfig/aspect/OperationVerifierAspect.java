package com.mislbd.report_manager.configuration.aopConfig.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mislbd.report_manager.configuration.annotation.Command;
import com.mislbd.report_manager.configuration.aopConfig.entity.TaskInstanceEntity;
import com.mislbd.report_manager.configuration.aopConfig.service.TaskInstanceService;
import com.mislbd.report_manager.configuration.commonService.CommonService;
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

import java.util.Map;

@Aspect
@Component
@Order(3)
public class OperationVerifierAspect {
    private final TaskInstanceService taskService;
    private final CommonService commonService;
    @Autowired
    private ApplicationContext context; //
    @Autowired
    private ObjectMapper objectMapper; // To serialize request body

    @Autowired
    private HttpServletRequest httpServletRequest;

    public OperationVerifierAspect(TaskInstanceService taskService, CommonService commonService) {
        this.taskService = taskService;
        this.commonService = commonService;
    }

    @Pointcut("@annotation(command)")
    public void verifyPointcut(Command command) {

    }

    @Around("verifyPointcut(command)")
    public Object verifyOperation(ProceedingJoinPoint joinPoint, Command command) throws Throwable {
        String operationName = command.value();
        Object[] args = joinPoint.getArgs();
        String initiator = SecurityContextHolder.getContext().getAuthentication().getName();; // or JWT

        boolean requiresApproval = checkIfApprovalRequired(operationName);

        if (requiresApproval) {
            // Simulate saving request for approval

            String payload = objectMapper.writeValueAsString(args[0]);

          Long taskId =  savePendingApproval(operationName, initiator, payload);

            // return fake success response (as if saved and will be verified)
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Task sent for verification\n Task id is: " +taskId
            ));
        }

        // No approval required, proceed with original method
        return joinPoint.proceed();
    }

    private boolean checkIfApprovalRequired(String operationName) {
        // ✅ You can check from DB or hardcoded list
        return true;
    }

    private Long savePendingApproval(String operation, String user, String payload) {
        TaskInstanceEntity task=new TaskInstanceEntity();
        task.setMaker(user);
        task.setPayload(payload);
        return  taskService.saveTaskInstance(task);
    }





}
