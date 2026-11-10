package com.mislbd.report_manager.sprint_manager.repository;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskNoteHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Repository for SprintTaskNoteHistoryEntity. */
@Repository
public interface SprintTaskNoteHistoryRepository extends JpaRepository<SprintTaskNoteHistoryEntity, Long>, JpaSpecificationExecutor<SprintTaskNoteHistoryEntity> {
}
