package com.mislbd.report_manager.sprint_manager.service;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskNoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SprintTaskNoteService {
    SprintTaskNoteEntity save(SprintTaskNoteEntity entity);
    SprintTaskNoteEntity update(Long id, SprintTaskNoteEntity entity);
    SprintTaskNoteEntity getById(Long id);
    List<SprintTaskNoteEntity> getAll();
    Page<SprintTaskNoteEntity> getAll(Pageable pageable);
}
