package com.mislbd.report_manager.controller.customer;
import com.mislbd.report_manager.configuration.aopConfig.processor.ApprovalTaskProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("customer/task")
public class TaskApprovalController {

    @Autowired
    private ApprovalTaskProcessor processor;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyTask(
            @RequestParam String operation,
            @RequestParam Long taskId,
            @RequestParam String action


    ) {
        processor.verifyOperation(operation, taskId, action);
        return ResponseEntity.ok("?? Action "  + " executed for " + operation);
    }
}