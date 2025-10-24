package com.mislbd.report_manager.configuration.aopConfig.processor;

import com.mislbd.report_manager.configuration.annotation.CommandAttribute;
import com.mislbd.report_manager.configuration.aopConfig.entity.CommandEntity;
import com.mislbd.report_manager.configuration.aopConfig.repository.CommandRepository;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

@Component
public class CommandAnnotationScanner {


    private final CommandRepository commandRepository ;

    public CommandAnnotationScanner( CommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    @Transactional
    public void scanAndSave() {
        // 🔍 Scan your base package
        Reflections reflections = new Reflections("com.mislbd.report_manager");

        // Find all classes annotated with CommandAttributeTest
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(CommandAttribute.class);

        for (Class<?> clazz : annotatedClasses) {
            CommandAttribute annotation = clazz.getAnnotation(CommandAttribute.class);
            String commandName = clazz.getSimpleName();
            CommandEntity metadata = new CommandEntity();
            boolean exists = commandRepository.existsByCommandName(commandName);
            if (exists) {
                metadata=commandRepository.findByCommandName(commandName);
            }

            metadata.setCommandPackageName(clazz.getName());
            metadata.setEntityPackageName(getEntityFullClassNameFromCommandClass(clazz.getName()));
            metadata.setDescription(annotation.description());
            metadata.setModuleName(annotation.module());
            metadata.setCommandName(clazz.getSimpleName());

            commandRepository.save(metadata);
        }
    }

    public static String getEntityFullClassNameFromCommandClass(String commandClassName) {
        try {
            Class<?> commandClass = Class.forName(commandClassName);
            Type genericSuperclass = commandClass.getGenericSuperclass();

            if (genericSuperclass instanceof ParameterizedType parameterizedType) {
                Type typeArg = parameterizedType.getActualTypeArguments()[0];
                if (typeArg instanceof Class<?> clazz) {
                    return clazz.getName(); // ✅ full name with package
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
