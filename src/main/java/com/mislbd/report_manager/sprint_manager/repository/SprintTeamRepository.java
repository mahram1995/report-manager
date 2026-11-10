package com.mislbd.report_manager.sprint_manager.repository;

import com.mislbd.report_manager.sprint_manager.entity.SprintTeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Repository for SprintTeamEntity. */
@Repository
public interface SprintTeamRepository extends JpaRepository<SprintTeamEntity, Long>, JpaSpecificationExecutor<SprintTeamEntity> {
}
