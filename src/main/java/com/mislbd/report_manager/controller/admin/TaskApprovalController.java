package com.mislbd.report_manager.controller.admin;
import com.mislbd.report_manager.configuration.aopConfig.processor.ApprovalTaskProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/task")
public class TaskApprovalController {

    @Autowired
    private ApprovalTaskProcessor processor;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyTask(
            @RequestParam("OperationName")  String operation,
            @RequestParam("taskId")  Long taskId,
            @RequestParam("actionName")  String action


    ) {
      return   processor.verifyOperation(operation, taskId, action);

    }
}