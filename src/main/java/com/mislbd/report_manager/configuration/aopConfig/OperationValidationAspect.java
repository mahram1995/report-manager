package com.mislbd.report_manager.configuration.aopConfig;

import com.mislbd.report_manager.configuration.annotation.ValidateAttribute;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
@Order(1) // run before verifier
public class OperationValidationAspect {

    @Autowired
    private ApplicationContext context;

    @Pointcut("@annotation(validateAttribute)")
    public void validationPointcut(ValidateAttribute validateAttribute) {}

    @Around("validationPointcut(validateAttribute)")
    public Object handleValidation(ProceedingJoinPoint joinPoint, ValidateAttribute validateAttribute) throws Throwable {
        String operation = validateAttribute.operation();

        // Find all validators in the context
        Map<String, OperationValidator> validators = context.getBeansOfType(OperationValidator.class);

        OperationValidator matchingValidator = validators.values().stream()
                .filter(v -> v.operationName().equals(operation))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No validator found for operation: " + operation));

        // Validate the first argument (you can enhance this to support multiple)
        Object[] args = joinPoint.getArgs();
        boolean valid = matchingValidator.validate(args[0]);

        if (!valid) {
            throw new RuntimeException("Validation failed for operation: " + operation);
        }

        return joinPoint.proceed();
    }
}

