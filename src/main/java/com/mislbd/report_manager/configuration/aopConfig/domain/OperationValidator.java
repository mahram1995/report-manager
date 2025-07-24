package com.mislbd.report_manager.configuration.aopConfig.domain;

public interface OperationValidator<T> {
    boolean validate(T domain);
}
