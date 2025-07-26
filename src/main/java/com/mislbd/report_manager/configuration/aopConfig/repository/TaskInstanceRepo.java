package com.mislbd.report_manager.configuration.aopConfig.repository;

import com.mislbd.report_manager.configuration.aopConfig.entity.TaskInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskInstanceRepo extends JpaRepository<TaskInstanceEntity, Long> {
    TaskInstanceEntity findByTaskId(Long taskId);
}
