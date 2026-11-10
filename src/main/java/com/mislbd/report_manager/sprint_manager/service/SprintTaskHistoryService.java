package com.mislbd.report_manager.sprint_manager.service;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SprintTaskHistoryService {
    SprintTaskHistoryEntity save(SprintTaskHistoryEntity entity);
    SprintTaskHistoryEntity update(Long id, SprintTaskHistoryEntity entity);
    SprintTaskHistoryEntity getById(Long id);
    List<SprintTaskHistoryEntity> getAll();
    Page<SprintTaskHistoryEntity> getAll(Pageable pageable);
}
