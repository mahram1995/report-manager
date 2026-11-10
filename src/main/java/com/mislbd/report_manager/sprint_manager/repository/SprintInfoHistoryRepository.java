package com.mislbd.report_manager.sprint_manager.repository;

import com.mislbd.report_manager.sprint_manager.entity.SprintInfoHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Repository for SprintInfoHistoryEntity. */
@Repository
public interface SprintInfoHistoryRepository extends JpaRepository<SprintInfoHistoryEntity, Long>, JpaSpecificationExecutor<SprintInfoHistoryEntity> {
}
