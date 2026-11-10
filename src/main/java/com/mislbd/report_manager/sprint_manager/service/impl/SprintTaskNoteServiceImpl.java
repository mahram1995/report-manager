package com.mislbd.report_manager.sprint_manager.service.impl;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskNoteEntity;
import com.mislbd.report_manager.sprint_manager.repository.SprintTaskNoteRepository;
import com.mislbd.report_manager.sprint_manager.service.SprintTaskNoteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SprintTaskNoteServiceImpl implements SprintTaskNoteService {
    private final SprintTaskNoteRepository repository;

    public SprintTaskNoteServiceImpl(SprintTaskNoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public SprintTaskNoteEntity save(SprintTaskNoteEntity entity) {
        return repository.save(entity);
    }

    @Override
    public SprintTaskNoteEntity update(Long id, SprintTaskNoteEntity entity) {
        SprintTaskNoteEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTaskNoteEntity not found with id: " + id));
        BeanUtils.copyProperties(entity, existing, "id");
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SprintTaskNoteEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTaskNoteEntity not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintTaskNoteEntity> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SprintTaskNoteEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
