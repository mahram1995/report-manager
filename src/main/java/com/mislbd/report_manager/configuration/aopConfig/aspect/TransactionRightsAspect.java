package com.mislbd.report_manager.configuration.aopConfig.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(1)
public class TransactionRightsAspect {

    @Autowired
    private HttpServletRequest request;

    @Before("execution(* com.mislbd.report_manager.serviceImpl..*(..))")
    public void verifyUserTransactionMakingRight(JoinPoint joinPoint) throws Throwable {
        String username = request.getHeader("username"); // Or extract from JWT token

        // Get method and parameters
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();

        // Optional: inspect specific annotation or class if needed
        // Example: if (!method.isAnnotationPresent(AllowTransaction.class)) { ... }

        // Call your logic to verify user's access
        boolean allowed = checkUserPermission(username, method.getName(), args);
        if (!allowed) {
            throw new AccessDeniedException("User not allowed to perform this transaction");
        }
    }

    private boolean checkUserPermission(String username, String methodName, Object[] args) {
        for (Object arg : args) {
            System.out.println("Request param: " + arg);
        }
        // 🔐 Replace this with real logic to check permission
        if ("restrictedMethod".equals(methodName)) {
            return false;
        }
        return true;
    }
}
