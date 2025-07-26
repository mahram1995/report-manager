package com.mislbd.report_manager.configuration.aopConfig.service;

import com.mislbd.report_manager.configuration.aopConfig.entity.TaskInstanceEntity;
import org.springframework.stereotype.Service;

@Service
public interface TaskInstanceService {
    public Long saveTaskInstance(TaskInstanceEntity entity);
    public TaskInstanceEntity getTaskByUserName(String userName);
    public TaskInstanceEntity getTaskByTaskId(Long taskId);
    public void deleteTaskByTaskId (Long taskId);
}
