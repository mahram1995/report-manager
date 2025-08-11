package com.mislbd.report_manager.controller.admin;
import com.mislbd.report_manager.configuration.aopConfig.entity.TaskInstanceEntity;
import com.mislbd.report_manager.configuration.aopConfig.processor.ApprovalTaskProcessor;
import com.mislbd.report_manager.configuration.aopConfig.service.TaskInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

@RestController
@RequestMapping("admin/task")
public class TaskInstanceController {
    private  final TaskInstanceService taskService;

    public TaskInstanceController(TaskInstanceService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/verify-operation")
    public ResponseEntity<?> verifyTask(
            @RequestParam(value = "taskId", required = true)  Long taskId,
            @RequestParam(value = "actionName" ,required = true)  String action,
            @RequestParam(value = "delegateUser", required = false)  String delegateUser
    ) {
      return   taskService.verifyOperation( taskId, action,delegateUser);
    }

    @GetMapping("/get-tasks")
    public Page<TaskInstanceEntity> getTasks(
            @RequestParam( name = "taskId",required = false) Long taskId,
            @RequestParam(name = "verifier",required = false) String verifier,
            @RequestParam(name = "maker",required = false) String maker,
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "size",defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return taskService.getTask(taskId, verifier, maker, pageable);
    }

    @GetMapping(  path = {"get-tasks-instance-payload/{taskId}"})
    public ResponseEntity getApprovalFlowTaskByTaskId(@PathVariable("taskId") Long taskId)  {
        return  ResponseEntity.ok(this.taskService.getApprovalFlowTaskPayload(taskId)) ;
    }


}