package com.mislbd.report_manager.sprint_manager.service;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SprintTaskService {
    SprintTaskEntity save(SprintTaskEntity entity);
    SprintTaskEntity update(Long id, SprintTaskEntity entity);
    SprintTaskEntity getById(Long id);
    List<SprintTaskEntity> getAll();
    Page<SprintTaskEntity> getAll(Pageable pageable);
}
