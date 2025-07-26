package com.mislbd.report_manager.configuration.aopConfig.service;

import com.mislbd.report_manager.configuration.aopConfig.processor.ApprovalTaskProcessor;
import org.springframework.stereotype.Component;

@Component
public class VerifierService {
    private final ApprovalTaskProcessor processor;

    public VerifierService(ApprovalTaskProcessor processor) {
        this.processor = processor;
    }

    public void verifyOperation(String operation, Long taskId, String action){
        processor.verifyOperation(operation, taskId, action);
    }
}
