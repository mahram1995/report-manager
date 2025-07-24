package com.mislbd.report_manager.configuration.aopConfig;

public interface OperationValidator {
    String operationName(); // unique name like "CREATE_NEW_USER"

    boolean validate(Object request); // you can customize this further
}
