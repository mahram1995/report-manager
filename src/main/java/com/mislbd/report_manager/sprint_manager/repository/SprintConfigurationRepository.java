package com.mislbd.report_manager.sprint_manager.repository;

import com.mislbd.report_manager.sprint_manager.entity.SprintConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Repository for SprintConfigurationEntity. */
@Repository
public interface SprintConfigurationRepository extends JpaRepository<SprintConfigurationEntity, Long>, JpaSpecificationExecutor<SprintConfigurationEntity> {
}
