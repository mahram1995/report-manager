package com.mislbd.report_manager.configuration.aopConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mislbd.report_manager.configuration.annotation.Command;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
@Order(2)
public class VerifierAspect {

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
        String initiator = httpServletRequest.getHeader("username"); // or JWT

        boolean requiresApproval = checkIfApprovalRequired(operationName);

        if (requiresApproval) {
            // Simulate saving request for approval
            Object[] args = joinPoint.getArgs();
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
