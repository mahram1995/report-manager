package com.mislbd.report_manager.configuration.aopConfig.processor;

import com.mislbd.report_manager.configuration.annotation.ApprovalFlowTaskListener;
import com.mislbd.report_manager.configuration.annotation.OnApprove;
import com.mislbd.report_manager.configuration.annotation.OnCorrection;
import com.mislbd.report_manager.configuration.annotation.OnRejection;
import com.mislbd.report_manager.configuration.aopConfig.service.TaskInstanceService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;

@Component
public class ApprovalTaskProcessor implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final TaskInstanceService taskService;
    private final Map<String, Object> operationHandlerMap = new HashMap<>();

    public ApprovalTaskProcessor(TaskInstanceService taskService) {
        this.taskService = taskService;
    }

    @PostConstruct
    public void init() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ApprovalFlowTaskListener.class);
        for (Object bean : beans.values()) {
            ApprovalFlowTaskListener listener = bean.getClass().getAnnotation(ApprovalFlowTaskListener.class);
            operationHandlerMap.put(listener.operation(), bean);
        }
    }

    public void verifyOperation(String operation, Long taskId, String action) {
        Object handler = operationHandlerMap.get(operation);
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for operation: " + operation);
        }

        for (Method method : handler.getClass().getDeclaredMethods()) {
            if (matchesAction(method, action)) {
                try {
                    method.setAccessible(true);
                    String payload=taskService.getTaskByTaskId(taskId).getPayload();

                    if (method.getParameterCount() == 1) {
                        method.invoke(handler, payload);
                    } else {
                        throw new IllegalArgumentException("Method must accept exactly one parameter (the payload).");
                    }

                    return;
                } catch (Exception e) {
                    throw new RuntimeException("Failed to execute approval method", e);
                }
            }
        }

        throw new RuntimeException("No method found for action: " + action + " in operation: " + operation);
    }

    private boolean matchesAction(Method method, String action) {
        return switch (action.toUpperCase()) {
            case "APPROVE" -> method.isAnnotationPresent(OnApprove.class);
            case "CORRECTION" -> method.isAnnotationPresent(OnCorrection.class);
            case "REJECT" -> method.isAnnotationPresent(OnRejection.class);
            default -> false;
        };
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
