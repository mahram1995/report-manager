package com.mislbd.report_manager.configuration.aopConfig.processor;

import com.mislbd.report_manager.configuration.annotation.CommandAggregate;
import com.mislbd.report_manager.configuration.annotation.CommandHandler;
import com.mislbd.report_manager.configuration.annotation.CommandValidator;
import com.mislbd.report_manager.configuration.annotation.CommandValidatorAggregate;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class CommandValidatorAnnotationProcessor {
    private final Map<Class<?>, HandlerMethod> commandHandlers = new HashMap<>();
    private final ApplicationContext context;

    public CommandValidatorAnnotationProcessor(ApplicationContext context) {
        this.context = context;
        initializeHandlers();
    }

    private void initializeHandlers() {
        Map<String, Object> aggregates = context.getBeansWithAnnotation(CommandValidatorAggregate.class);

        for (Object bean : aggregates.values()) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            for (Method method : targetClass.getMethods()) {
                if (method.isAnnotationPresent(CommandValidator.class) && method.getParameterCount() == 1) {
                    Class<?> commandType = method.getParameterTypes()[0];
                    commandHandlers.put(commandType, new HandlerMethod(bean, method));
                }
            }
        }
    }
    public boolean runCommandValidator(Object command){
        HandlerMethod handler = commandHandlers.entrySet().stream()
                .filter(e -> e.getKey().isAssignableFrom(command.getClass()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
                //.orElseThrow(() -> new RuntimeException("No validator found for: " + command.getClass().getSimpleName()));

        // No validator found, skip validation
        if (handler == null) {
            return true;
        }

        try {
            Object proxyBean = context.getBean(handler.bean.getClass()); // Spring proxy
             Boolean response= (Boolean) handler.method.invoke(proxyBean, command);
            return response;
        }catch (InvocationTargetException ite) {
            throw new RuntimeException(ite.getTargetException().getMessage(), ite.getTargetException());
        } catch (Exception e) {
            throw new RuntimeException("Error invoking command validator", e);
        }
    };

    private record HandlerMethod(Object bean, Method method) {}
}
