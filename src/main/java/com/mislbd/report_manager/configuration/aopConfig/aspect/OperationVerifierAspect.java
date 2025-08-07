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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
        HttpServletRequest request = getCurrentHttpRequest();
        String detailsUI = request.getHeader("detailsUI");
        String correctionUI = request.getHeader("correctionUI");
        String verifier = request.getHeader("verifier");
        String initiator = SecurityContextHolder.getContext().getAuthentication().getName();; // or JWT

        boolean requiresApproval = checkIfApprovalRequired(operationName);

        if (requiresApproval) {
            // Simulate saving request for approval

            String payload = objectMapper.writeValueAsString(args[0]);

          Long taskId =  savePendingApproval(operationName, initiator, payload, detailsUI, correctionUI,verifier);

            // return fake success response (as if saved and will be verified)
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Task sent for verification\n Task id is: " +taskId
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
        return true;
    }

    private Long savePendingApproval(String operation, String user, String payload,
                                     String detailsUI, String correctionUI, String verifier) {
        TaskInstanceEntity task=new TaskInstanceEntity();
        task.setMaker(user);
        task.setCommandName(operation);
        task.setPayload(payload);
        task.setTaskDetailsUi(detailsUI);
        task.setTaskCorrectionUi(correctionUI);
        task.setVerifier(verifier);
        return  taskService.saveTaskInstance(task);
    }





}
