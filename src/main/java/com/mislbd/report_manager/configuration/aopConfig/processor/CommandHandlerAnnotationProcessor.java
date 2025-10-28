package com.mislbd.report_manager.configuration.aopConfig.processor;

import com.mislbd.report_manager.configuration.annotation.CommandAggregate;
import com.mislbd.report_manager.configuration.annotation.CommandHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class CommandHandlerAnnotationProcessor {
    private final Map<Class<?>, HandlerMethod> commandHandlers = new HashMap<>();
    private final ApplicationContext context;

    public CommandHandlerAnnotationProcessor(ApplicationContext context) {
        this.context = context;
        initializeHandlers();
    }

    private void initializeHandlers() {
        Map<String, Object> aggregates = context.getBeansWithAnnotation(CommandAggregate.class);

        for (Object bean : aggregates.values()) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            for (Method method : targetClass.getMethods()) {
                if (method.isAnnotationPresent(CommandHandler.class) && method.getParameterCount() == 1) {
                    Class<?> commandType = method.getParameterTypes()[0];
                    commandHandlers.put(commandType, new HandlerMethod(bean, method));
                    System.out.println("Registered handler: " + method.getName() +
                            " for command: " + commandType.getSimpleName() +
                            " in bean: " + bean.getClass().getSimpleName());
                }
            }
        }
    }
    public Object runCommand(Object command){



        HandlerMethod handler = commandHandlers.entrySet().stream()
                .filter(e -> e.getKey().isAssignableFrom(command.getClass()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No handler found for: " + command.getClass().getSimpleName()));

       try {
           Object proxyBean = context.getBean(handler.bean.getClass()); // Spring proxy
           // invoke the handler method for doing operation
           return handler.method.invoke(proxyBean, command);
       }catch (InvocationTargetException ite) {
           throw new RuntimeException("Error executing command: " + ite.getTargetException().getMessage(), ite.getTargetException());
       } catch (Exception e) {
           throw new RuntimeException("Error invoking command handler", e);
       }
    };

    private record HandlerMethod(Object bean, Method method) {}

}
