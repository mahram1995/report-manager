package com.mislbd.report_manager.sprint_manager.repository;

import com.mislbd.report_manager.sprint_manager.entity.SprintTeamConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Repository for SprintTeamConfigurationEntity. */
@Repository
public interface SprintTeamConfigurationRepository extends JpaRepository<SprintTeamConfigurationEntity, Long>, JpaSpecificationExecutor<SprintTeamConfigurationEntity> {
}
