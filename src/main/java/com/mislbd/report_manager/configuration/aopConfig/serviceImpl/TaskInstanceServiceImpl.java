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
    public Long saveTaskInstance(TaskInstanceEntity entity) {
        if(entity.getMaker()==null){
            throw new RuntimeException("Maker id can't be null");
        }
        taskRepo.save(entity);
        return entity.getTaskId();
    }

    @Override
    public TaskInstanceEntity getTaskByUserName(String userName) {
        return null;
    }

    @Override
    public TaskInstanceEntity getTaskByTaskId(Long taskId) {
        return taskRepo.findByTaskId(taskId);
    }

    @Override
    public void deleteTaskByTaskId(Long taskId) {
        taskRepo.deleteByTaskId(taskId);
    }
}
