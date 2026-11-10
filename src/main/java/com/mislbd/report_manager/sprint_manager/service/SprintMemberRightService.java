package com.mislbd.report_manager.sprint_manager.service;

import com.mislbd.report_manager.sprint_manager.entity.SprintMemberRightEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SprintMemberRightService {
    SprintMemberRightEntity save(SprintMemberRightEntity entity);
    SprintMemberRightEntity update(Long id, SprintMemberRightEntity entity);
    SprintMemberRightEntity getById(Long id);
    List<SprintMemberRightEntity> getAll();
    Page<SprintMemberRightEntity> getAll(Pageable pageable);
}
