package com.mislbd.report_manager.sprint_manager.repository;

import com.mislbd.report_manager.sprint_manager.entity.SprintInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Repository for SprintInfoEntity. */
@Repository
public interface SprintInfoRepository extends JpaRepository<SprintInfoEntity, Long>, JpaSpecificationExecutor<SprintInfoEntity> {
}
