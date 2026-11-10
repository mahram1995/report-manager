package com.mislbd.report_manager.sprint_manager.service;

import com.mislbd.report_manager.sprint_manager.entity.SprintConfigurationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SprintConfigurationService {
    SprintConfigurationEntity save(SprintConfigurationEntity entity);
    SprintConfigurationEntity update(Long id, SprintConfigurationEntity entity);
    SprintConfigurationEntity getById(Long id);
    List<SprintConfigurationEntity> getAll();
    Page<SprintConfigurationEntity> getAll(Pageable pageable);
}
