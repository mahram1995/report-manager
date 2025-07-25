package com.mislbd.report_manager.configuration.aopConfig.serviceImpl;

import com.mislbd.report_manager.configuration.aopConfig.entity.TaskInstanceEntity;
import com.mislbd.report_manager.configuration.aopConfig.repository.TaskInstanceRepo;
import com.mislbd.report_manager.configuration.aopConfig.service.TaskInstanceService;
import com.mislbd.report_manager.exception.CommonException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskInstanceServiceImpl implements TaskInstanceService {
    private final TaskInstanceRepo taskRepo;

    public TaskInstanceServiceImpl(TaskInstanceRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    @Override
    public ResponseEntity<?> saveTaskInstance(TaskInstanceEntity entity) {
        if(entity.getMaker()==null){
            throw new RuntimeException("Maker id can't be null");
        }
        taskRepo.save(entity);
        return ResponseEntity.ok("Task sent to verifier,\n Task id is "+ entity.getTaskId());
    }

    @Override
    public TaskInstanceEntity getTaskByUserName(String userName) {
        return null;
    }
}
