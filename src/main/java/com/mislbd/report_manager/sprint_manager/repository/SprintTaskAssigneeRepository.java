package com.mislbd.report_manager.sprint_manager.repository;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskAssigneeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Repository for SprintTaskAssigneeEntity. */
@Repository
public interface SprintTaskAssigneeRepository extends JpaRepository<SprintTaskAssigneeEntity, Long>, JpaSpecificationExecutor<SprintTaskAssigneeEntity> {
}
