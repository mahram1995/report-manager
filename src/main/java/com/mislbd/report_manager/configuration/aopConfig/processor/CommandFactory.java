package com.mislbd.report_manager.configuration.aopConfig.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Constructor;

public class CommandFactory {

    private static final String COMMAND_PACKAGE = "com.mislbd.report_manager.command";
    private static final String ENTITY_PACKAGE = "com.mislbd.report_manager.entity.admin";

    public static Object buildCommand(String commandClassName, String entityClassName, Object payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            Class<?> commandClass = Class.forName(COMMAND_PACKAGE + "." + commandClassName);
            Class<?> entityClass = Class.forName(ENTITY_PACKAGE + "." + entityClassName);

            Object entityPayload = mapper.convertValue(payload, entityClass);

            Constructor<?> constructor = commandClass.getConstructor(entityClass);
            return constructor.newInstance(entityPayload);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to build command " + commandClassName, e);
        }
    }
}

