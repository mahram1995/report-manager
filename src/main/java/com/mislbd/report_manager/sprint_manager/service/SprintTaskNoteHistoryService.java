package com.mislbd.report_manager.sprint_manager.service;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskNoteHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SprintTaskNoteHistoryService {
    SprintTaskNoteHistoryEntity save(SprintTaskNoteHistoryEntity entity);
    SprintTaskNoteHistoryEntity update(Long id, SprintTaskNoteHistoryEntity entity);
    SprintTaskNoteHistoryEntity getById(Long id);
    List<SprintTaskNoteHistoryEntity> getAll();
    Page<SprintTaskNoteHistoryEntity> getAll(Pageable pageable);
}
