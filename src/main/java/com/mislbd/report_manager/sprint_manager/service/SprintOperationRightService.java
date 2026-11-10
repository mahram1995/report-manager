package com.mislbd.report_manager.sprint_manager.service;

import com.mislbd.report_manager.sprint_manager.entity.SprintOperationRightEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SprintOperationRightService {
    SprintOperationRightEntity save(SprintOperationRightEntity entity);
    SprintOperationRightEntity update(Long id, SprintOperationRightEntity entity);
    SprintOperationRightEntity getById(Long id);
    List<SprintOperationRightEntity> getAll();
    Page<SprintOperationRightEntity> getAll(Pageable pageable);
}
