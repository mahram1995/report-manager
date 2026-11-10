package com.mislbd.report_manager.sprint_manager.service;

import com.mislbd.report_manager.sprint_manager.entity.SprintTeamConfigurationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SprintTeamConfigurationService {
    SprintTeamConfigurationEntity save(SprintTeamConfigurationEntity entity);
    SprintTeamConfigurationEntity update(Long id, SprintTeamConfigurationEntity entity);
    SprintTeamConfigurationEntity getById(Long id);
    List<SprintTeamConfigurationEntity> getAll();
    Page<SprintTeamConfigurationEntity> getAll(Pageable pageable);
}
