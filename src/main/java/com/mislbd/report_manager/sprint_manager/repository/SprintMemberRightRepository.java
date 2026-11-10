package com.mislbd.report_manager.sprint_manager.repository;

import com.mislbd.report_manager.sprint_manager.entity.SprintMemberRightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Repository for SprintMemberRightEntity. */
@Repository
public interface SprintMemberRightRepository extends JpaRepository<SprintMemberRightEntity, Long>, JpaSpecificationExecutor<SprintMemberRightEntity> {
}
