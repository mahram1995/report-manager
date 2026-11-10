package com.mislbd.report_manager.sprint_manager.service.impl;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskNoteHistoryEntity;
import com.mislbd.report_manager.sprint_manager.repository.SprintTaskNoteHistoryRepository;
import com.mislbd.report_manager.sprint_manager.service.SprintTaskNoteHistoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SprintTaskNoteHistoryServiceImpl implements SprintTaskNoteHistoryService {
    private final SprintTaskNoteHistoryRepository repository;

    public SprintTaskNoteHistoryServiceImpl(SprintTaskNoteHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public SprintTaskNoteHistoryEntity save(SprintTaskNoteHistoryEntity entity) {
        return repository.save(entity);
    }

    @Override
    public SprintTaskNoteHistoryEntity update(Long id, SprintTaskNoteHistoryEntity entity) {
        SprintTaskNoteHistoryEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTaskNoteHistoryEntity not found with id: " + id));
        BeanUtils.copyProperties(entity, existing, "id");
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SprintTaskNoteHistoryEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTaskNoteHistoryEntity not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintTaskNoteHistoryEntity> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SprintTaskNoteHistoryEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
