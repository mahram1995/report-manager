package com.mislbd.report_manager.sprint_manager.repository;

import com.mislbd.report_manager.sprint_manager.entity.SprintAssignmentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Repository for SprintAssignmentHistoryEntity. */
@Repository
public interface SprintAssignmentHistoryRepository extends JpaRepository<SprintAssignmentHistoryEntity, Long>, JpaSpecificationExecutor<SprintAssignmentHistoryEntity> {
}
