package com.mislbd.report_manager.sprint_manager.service;

import com.mislbd.report_manager.sprint_manager.entity.SprintInfoHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SprintInfoHistoryService {
    SprintInfoHistoryEntity save(SprintInfoHistoryEntity entity);
    SprintInfoHistoryEntity update(Long id, SprintInfoHistoryEntity entity);
    SprintInfoHistoryEntity getById(Long id);
    List<SprintInfoHistoryEntity> getAll();
    Page<SprintInfoHistoryEntity> getAll(Pageable pageable);
}
