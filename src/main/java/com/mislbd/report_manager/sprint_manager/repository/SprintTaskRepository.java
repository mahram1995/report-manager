package com.mislbd.report_manager.sprint_manager.repository;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Repository for SprintTaskEntity. */
@Repository
public interface SprintTaskRepository extends JpaRepository<SprintTaskEntity, Long>, JpaSpecificationExecutor<SprintTaskEntity> {
}
