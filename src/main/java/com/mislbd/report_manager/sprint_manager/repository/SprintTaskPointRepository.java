package com.mislbd.report_manager.sprint_manager.repository;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Repository for SprintTaskPointEntity. */
@Repository
public interface SprintTaskPointRepository extends JpaRepository<SprintTaskPointEntity, Long>, JpaSpecificationExecutor<SprintTaskPointEntity> {
}
