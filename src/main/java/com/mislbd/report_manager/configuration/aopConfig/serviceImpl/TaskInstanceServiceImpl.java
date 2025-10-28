package com.mislbd.report_manager.configuration.aopConfig.serviceImpl;

import com.mislbd.report_manager.configuration.aopConfig.entity.TaskInstanceEntity;
import com.mislbd.report_manager.configuration.aopConfig.processor.ApprovalTaskProcessor;
import com.mislbd.report_manager.configuration.aopConfig.repository.TaskInstanceRepo;
import com.mislbd.report_manager.configuration.aopConfig.service.TaskInstanceService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;
import java.util.Map;

@Component
public class TaskInstanceServiceImpl implements TaskInstanceService {
    private final TaskInstanceRepo taskRepo;
    private  final ApprovalTaskProcessor processor;

    public TaskInstanceServiceImpl(TaskInstanceRepo taskRepo, ApprovalTaskProcessor processor) {
        this.taskRepo = taskRepo;

        this.processor = processor;
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
    public boolean existsTaskByDomainReference(String commandName, String reference) {
        return taskRepo.existsByCommandNameAndDomainReference(commandName,reference);
    }

    @Override
    public TaskInstanceEntity getTaskByTaskId(Long taskId) {
        return taskRepo.findByTaskId(taskId);
    }

    @Override
    public String getApprovalFlowTaskPayload(Long taskId) {
        TaskInstanceEntity  tasks= taskRepo.findByTaskId(taskId);
        Object rawPayload = tasks.getPayload();
        String payloadStr;

        if (rawPayload instanceof Clob) {
            payloadStr = clobToString((Clob) rawPayload);
        } else if (rawPayload != null) {
            payloadStr = rawPayload.toString();
        } else {
            payloadStr = null;
        }


        return payloadStr;
    }
    private String clobToString(Clob clob) {
        if (clob == null) return null;
        try (Reader reader = clob.getCharacterStream();
             BufferedReader br = new BufferedReader(reader)) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert CLOB to String", e);
        }
    }
    @Override
    public void deleteTaskByTaskId(Long taskId) {
        taskRepo.deleteByTaskId(taskId);
    }

    @Override
    @Transactional
    public ResponseEntity<?> verifyOperation(Long taskId, String action, String delegateUser) {
        TaskInstanceEntity task= getTaskByTaskId(taskId);


        Object response = null;
        if(action.contains("APPROVE")){
            response= processor.verifyOperation(task.getCommandName(), task.getPayload(), action);
           // taskRepo.deleteByTaskId(taskId);
        }else if (action.contains("CORRECTION")){
            task.setStatus("CORRECTION");
            taskRepo.save(task);
            return ResponseEntity.ok().body(Map.of("message", "Task send for correction successfully"));
        }else if (action.contains("REJECTION")){
            response= processor.verifyOperation(task.getCommandName(), task.getPayload(), action);
            taskRepo.deleteByTaskId(taskId);
        }else if(action.contains("DELEGATE")){
            task.setVerifier(delegateUser);
            taskRepo.save(task);
            return ResponseEntity.ok().body(Map.of("message", "Task delegate to " + delegateUser));
        }

      return (ResponseEntity<?>) response;
    }

    @Override
    public Page<TaskInstanceEntity> getTask(Long taskId, String verifier,
                                            String maker, String status,Pageable pageable
                                            ) {
        return this.taskRepo.getTasks(taskId, verifier, maker, status, pageable);
    }
}
