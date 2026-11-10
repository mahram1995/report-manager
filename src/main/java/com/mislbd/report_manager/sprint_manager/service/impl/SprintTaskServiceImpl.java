package com.mislbd.report_manager.sprint_manager.service.impl;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskEntity;
import com.mislbd.report_manager.sprint_manager.repository.SprintTaskRepository;
import com.mislbd.report_manager.sprint_manager.service.SprintTaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SprintTaskServiceImpl implements SprintTaskService {
    private final SprintTaskRepository repository;

    public SprintTaskServiceImpl(SprintTaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public SprintTaskEntity save(SprintTaskEntity entity) {
        return repository.save(entity);
    }

    @Override
    public SprintTaskEntity update(Long id, SprintTaskEntity entity) {
        SprintTaskEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTaskEntity not found with id: " + id));
        BeanUtils.copyProperties(entity, existing, "id");
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SprintTaskEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTaskEntity not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintTaskEntity> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SprintTaskEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
