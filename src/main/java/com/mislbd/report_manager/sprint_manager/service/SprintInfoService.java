package com.mislbd.report_manager.sprint_manager.service;

import com.mislbd.report_manager.sprint_manager.entity.SprintInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SprintInfoService {
    SprintInfoEntity save(SprintInfoEntity entity);
    SprintInfoEntity update(Long id, SprintInfoEntity entity);
    SprintInfoEntity getById(Long id);
    List<SprintInfoEntity> getAll();
    Page<SprintInfoEntity> getAll(Pageable pageable);
}
