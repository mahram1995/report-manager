package com.mislbd.report_manager.sprint_manager.service;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskPointEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SprintTaskPointService {
    SprintTaskPointEntity save(SprintTaskPointEntity entity);
    SprintTaskPointEntity update(Long id, SprintTaskPointEntity entity);
    SprintTaskPointEntity getById(Long id);
    List<SprintTaskPointEntity> getAll();
    Page<SprintTaskPointEntity> getAll(Pageable pageable);
}
