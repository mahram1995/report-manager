package com.mislbd.report_manager.configuration.aopConfig.aspect;

import com.mislbd.report_manager.configuration.annotation.Command;
import com.mislbd.report_manager.configuration.annotation.ValidateOperation;
import com.mislbd.report_manager.configuration.aopConfig.domain.OperationValidator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
@Order(2) // run before verifier
public class OperationValidationAspect {

    private final ApplicationContext context;

    public OperationValidationAspect(ApplicationContext context) {
        this.context = context;
    }

    @Pointcut("@annotation(com.mislbd.report_manager.configuration.annotation.Command)")
    public void commandPointcut() {}

    @Around("commandPointcut() && @annotation(command)")
    public Object validateOperationAndProceed(ProceedingJoinPoint joinPoint, Command command) throws Throwable {
        String operation = command.value();
        Object dto = joinPoint.getArgs()[0]; // assuming DTO is the first parameter

        boolean validated = invokeValidator(operation, dto);

        if (!validated) {
            throw new RuntimeException("Validation failed for operation: " + operation);
        }

        return joinPoint.proceed();
    }

    private boolean invokeValidator(String operation, Object dto) {
        Map<String, Object> beans = context.getBeansWithAnnotation(ValidateOperation.class);

        for (Object bean : beans.values()) {
            ValidateOperation annotation = bean.getClass().getAnnotation(ValidateOperation.class);
            if (annotation.operation().equalsIgnoreCase(operation)) {
                if (bean instanceof OperationValidator<?> validator) {
                    return ((OperationValidator<Object>) validator).validate(dto);
                }
            }
        }

        throw new RuntimeException("No validator found for operation: " + operation);
    }
}
