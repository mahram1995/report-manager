package com.mislbd.report_manager.sprint_manager.service;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskAssigneeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SprintTaskAssigneeService {
    SprintTaskAssigneeEntity save(SprintTaskAssigneeEntity entity);
    SprintTaskAssigneeEntity update(Long id, SprintTaskAssigneeEntity entity);
    SprintTaskAssigneeEntity getById(Long id);
    List<SprintTaskAssigneeEntity> getAll();
    Page<SprintTaskAssigneeEntity> getAll(Pageable pageable);
}
