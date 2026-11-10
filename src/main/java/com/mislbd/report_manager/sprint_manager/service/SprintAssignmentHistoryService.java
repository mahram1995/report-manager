package com.mislbd.report_manager.sprint_manager.service;

import com.mislbd.report_manager.sprint_manager.entity.SprintAssignmentHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SprintAssignmentHistoryService {
    SprintAssignmentHistoryEntity save(SprintAssignmentHistoryEntity entity);
    SprintAssignmentHistoryEntity update(Long id, SprintAssignmentHistoryEntity entity);
    SprintAssignmentHistoryEntity getById(Long id);
    List<SprintAssignmentHistoryEntity> getAll();
    Page<SprintAssignmentHistoryEntity> getAll(Pageable pageable);
}
