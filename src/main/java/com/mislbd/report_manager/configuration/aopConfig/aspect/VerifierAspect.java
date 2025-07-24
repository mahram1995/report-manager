package com.mislbd.report_manager.configuration.aopConfig.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mislbd.report_manager.configuration.annotation.Command;
import com.mislbd.report_manager.configuration.annotation.ValidateAttribute;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Aspect
@Component
@Order(3)
public class VerifierAspect {
    @Autowired
    private ApplicationContext context; //
    @Autowired
    private ObjectMapper objectMapper; // To serialize request body

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Pointcut("@annotation(command)")
    public void verifyPointcut(Command command) {

    }

    @Around("verifyPointcut(command)")
    public Object verifyOperation(ProceedingJoinPoint joinPoint, Command command) throws Throwable {
        String operationName = command.value();
        Object[] args = joinPoint.getArgs();
        String initiator = httpServletRequest.getHeader("username"); // or JWT

        boolean requiresApproval = checkIfApprovalRequired(operationName);

        if (requiresApproval) {
            // Simulate saving request for approval

            String payload = objectMapper.writeValueAsString(args[0]);

            savePendingApproval(operationName, initiator, payload);

            // return fake success response (as if saved and will be verified)
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Request submitted for verification."
            ));
        }

        // No approval required, proceed with original method
        return joinPoint.proceed();
    }

    private boolean checkIfApprovalRequired(String operationName) {
        // ✅ You can check from DB or hardcoded list
        return true;
    }

    private void savePendingApproval(String operation, String user, String payload) {
        // Save to DB or queue - you can define an Entity like ApprovalRequestEntity
        System.out.println("Saved approval request from " + user + " for: " + operation);
        System.out.println("Payload: " + payload);
    }


}
