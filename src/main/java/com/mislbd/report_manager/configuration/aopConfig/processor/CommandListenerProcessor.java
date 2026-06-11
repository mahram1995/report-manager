package com.mislbd.report_manager.configuration.aopConfig.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mislbd.report_manager.configuration.annotation.*;
import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class CommandListenerProcessor {
    private final ApplicationContext applicationContext;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Object> operationHandlerMap = new HashMap<>();
    public CommandListenerProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    //PostConstruct annotation scan in which classes use ApprovalFlowTaskListener and put all listener in a hasMap
    @PostConstruct
    public void init() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ApprovalFlowTaskListener.class);
        for (Object bean : beans.values()) {
            ApprovalFlowTaskListener listener = bean.getClass().getAnnotation(ApprovalFlowTaskListener.class);
            operationHandlerMap.put(listener.operation(), bean);
        }
    }
    public void publishCommandListener(String commandName, Object command, String action ){
        Object handler = operationHandlerMap.get(commandName);
        if(handler!=null){
            for (Method method : handler.getClass().getDeclaredMethods()) {
                if (matchesAction(method, action)) {
                    try {
                        method.setAccessible(true);

                        if (method.getParameterCount() == 1) {
                            Class<?> paramType = method.getParameterTypes()[0];



                            Object result = method.invoke(handler, command);



                        } else {
                            throw new IllegalArgumentException("Method must accept exactly one parameter (the payload).");
                        }

                    } catch (Exception e) {
                        throw new RuntimeException("Failed to execute command listener " + e.toString());
                    }
                }
            }
        }


    }

    private boolean matchesAction(Method method, String action) {
        return switch (action.toUpperCase()) {
            case "START" -> method.isAnnotationPresent(OnStart.class);
            case "APPROVE" -> method.isAnnotationPresent(OnApprove.class);
            case "CORRECTION" -> method.isAnnotationPresent(OnCorrection.class);
            case "REJECTION" -> method.isAnnotationPresent(OnRejection.class);
            default -> false;
        };
    }
}
