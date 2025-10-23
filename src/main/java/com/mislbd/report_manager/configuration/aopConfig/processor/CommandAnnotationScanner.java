package com.mislbd.report_manager.configuration.aopConfig.processor;

import com.mislbd.report_manager.configuration.annotation.CommandAttributeTest;
import com.mislbd.report_manager.configuration.aopConfig.entity.CommandMetadataEntity;
import com.mislbd.report_manager.configuration.aopConfig.repository.CommandMetadataRepository;
import org.aspectj.util.Reflection;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class CommandAnnotationScanner {

    private final CommandMetadataRepository repository;

    public CommandAnnotationScanner(CommandMetadataRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void scanAndSave() {
        // 🔍 Scan your base package
        Reflections reflections = new Reflections("com.mislbd.report_manager");

        // Find all classes annotated with CommandAttribute
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(CommandAttributeTest.class);

        for (Class<?> clazz : annotatedClasses) {
            CommandAttributeTest annotation = clazz.getAnnotation(CommandAttributeTest.class);
            String packageName = clazz.getPackageName();
            String className = clazz.getSimpleName();
            boolean exists = repository.existsByPackageNameAndClassName(packageName, className);
            if (exists) {
                continue; // Skip already saved command
            }
            CommandMetadataEntity metadata = new CommandMetadataEntity();
            metadata.setClassName(clazz.getSimpleName());
            metadata.setPackageName(clazz.getPackageName());
            metadata.setName(annotation.name());
            metadata.setDescription(annotation.description());
            metadata.setModule(annotation.module());

            repository.save(metadata);
        }
    }
}
