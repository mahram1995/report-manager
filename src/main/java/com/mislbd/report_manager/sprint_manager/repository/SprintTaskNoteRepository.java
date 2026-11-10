package com.mislbd.report_manager.sprint_manager.repository;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskNoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Repository for SprintTaskNoteEntity. */
@Repository
public interface SprintTaskNoteRepository extends JpaRepository<SprintTaskNoteEntity, Long>, JpaSpecificationExecutor<SprintTaskNoteEntity> {
}
