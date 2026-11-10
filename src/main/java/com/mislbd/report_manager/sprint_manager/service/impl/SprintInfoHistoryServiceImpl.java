package com.mislbd.report_manager.sprint_manager.service.impl;

import com.mislbd.report_manager.sprint_manager.entity.SprintInfoHistoryEntity;
import com.mislbd.report_manager.sprint_manager.service.SprintInfoHistoryService;
import com.mislbd.report_manager.sprint_manager.repository.SprintInfoHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SprintInfoHistoryServiceImpl implements SprintInfoHistoryService {
    private final SprintInfoHistoryRepository repository;

    public SprintInfoHistoryServiceImpl(SprintInfoHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public SprintInfoHistoryEntity save(SprintInfoHistoryEntity entity) {
        return repository.save(entity);
    }

    @Override
    public SprintInfoHistoryEntity update(Long id, SprintInfoHistoryEntity entity) {
        SprintInfoHistoryEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintInfoHistoryEntity not found with id: " + id));
        BeanUtils.copyProperties(entity, existing, "id");
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SprintInfoHistoryEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintInfoHistoryEntity not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintInfoHistoryEntity> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SprintInfoHistoryEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
