package com.mislbd.report_manager.sprint_manager.repository;

import com.mislbd.report_manager.sprint_manager.entity.SprintTeamMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Repository for SprintTeamMemberEntity. */
@Repository
public interface SprintTeamMemberRepository extends JpaRepository<SprintTeamMemberEntity, Long>, JpaSpecificationExecutor<SprintTeamMemberEntity> {
}
