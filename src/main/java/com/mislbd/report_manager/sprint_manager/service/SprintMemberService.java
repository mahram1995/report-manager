package com.mislbd.report_manager.sprint_manager.service;

import com.mislbd.report_manager.sprint_manager.entity.SprintMemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SprintMemberService {
    SprintMemberEntity save(SprintMemberEntity entity);
    SprintMemberEntity update(Long id, SprintMemberEntity entity);
    SprintMemberEntity getById(Long id);
    List<SprintMemberEntity> getAll();
    Page<SprintMemberEntity> getAll(Pageable pageable);
}
