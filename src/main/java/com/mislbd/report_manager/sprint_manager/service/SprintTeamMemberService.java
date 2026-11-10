package com.mislbd.report_manager.sprint_manager.service;

import com.mislbd.report_manager.sprint_manager.entity.SprintTeamMemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SprintTeamMemberService {
    SprintTeamMemberEntity save(SprintTeamMemberEntity entity);
    SprintTeamMemberEntity update(Long id, SprintTeamMemberEntity entity);
    SprintTeamMemberEntity getById(Long id);
    List<SprintTeamMemberEntity> getAll();
    Page<SprintTeamMemberEntity> getAll(Pageable pageable);
}
