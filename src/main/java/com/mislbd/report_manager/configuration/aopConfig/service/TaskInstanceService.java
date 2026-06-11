package com.mislbd.report_manager.configuration.aopConfig.service;

import com.mislbd.report_manager.configuration.aopConfig.entity.TaskInstanceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

@Service
public interface TaskInstanceService {
    public Long saveTaskInstance(TaskInstanceEntity entity);
    public TaskInstanceEntity getTaskByUserName(String userName);
    public boolean existsTaskByDomainReference(String commandName,String reference);
    public TaskInstanceEntity getTaskByTaskId(Long taskId);
    public TaskInstanceEntity getTaskByDomainRefAndCommandName(String commandName,String reference);
    String getApprovalFlowTaskPayload(Long taskId);

    public void deleteTaskByTaskId (Long taskId);
    public ResponseEntity<?>  verifyOperation( Long taskId, String action,String delegateUser);
    public Page<TaskInstanceEntity> getTask(Long taskId, String verifier,
                                            String maker,String status, Pageable pageable
                                             );


}
