package com.mislbd.report_manager.sprint_manager.repository;

import com.mislbd.report_manager.sprint_manager.entity.SprintMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Repository for SprintMemberEntity. */
@Repository
public interface SprintMemberRepository extends JpaRepository<SprintMemberEntity, Long>, JpaSpecificationExecutor<SprintMemberEntity> {
}
