package com.mislbd.report_manager.configuration.aopConfig.service;

import com.mislbd.report_manager.configuration.aopConfig.entity.TaskInstanceEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface TaskInstanceService {
    public ResponseEntity<?> saveTaskInstance(TaskInstanceEntity entity);
    public TaskInstanceEntity getTaskByUserName(String userName);
}
