package com.mislbd.report_manager.sprint_manager.service.impl;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskHistoryEntity;
import com.mislbd.report_manager.sprint_manager.service.SprintTaskHistoryService;
import com.mislbd.report_manager.sprint_manager.repository.SprintTaskHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SprintTaskHistoryServiceImpl implements SprintTaskHistoryService {
    private final SprintTaskHistoryRepository repository;

    public SprintTaskHistoryServiceImpl(SprintTaskHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public SprintTaskHistoryEntity save(SprintTaskHistoryEntity entity) {
        return repository.save(entity);
    }

    @Override
    public SprintTaskHistoryEntity update(Long id, SprintTaskHistoryEntity entity) {
        SprintTaskHistoryEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTaskHistoryEntity not found with id: " + id));
        BeanUtils.copyProperties(entity, existing, "id");
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SprintTaskHistoryEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTaskHistoryEntity not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintTaskHistoryEntity> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SprintTaskHistoryEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
