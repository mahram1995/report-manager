package com.mislbd.report_manager.sprint_manager.service;

import com.mislbd.report_manager.sprint_manager.entity.SprintTeamEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SprintTeamService {
    SprintTeamEntity save(SprintTeamEntity entity);
    SprintTeamEntity update(Long id, SprintTeamEntity entity);
    SprintTeamEntity getById(Long id);
    List<SprintTeamEntity> getAll();
    Page<SprintTeamEntity> getAll(Pageable pageable);
}
