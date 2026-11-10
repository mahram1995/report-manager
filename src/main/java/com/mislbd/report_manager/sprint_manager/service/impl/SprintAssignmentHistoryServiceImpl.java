package com.mislbd.report_manager.sprint_manager.service.impl;

import com.mislbd.report_manager.sprint_manager.entity.SprintAssignmentHistoryEntity;
import com.mislbd.report_manager.sprint_manager.service.SprintAssignmentHistoryService;
import com.mislbd.report_manager.sprint_manager.repository.SprintAssignmentHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SprintAssignmentHistoryServiceImpl implements SprintAssignmentHistoryService {
    private final SprintAssignmentHistoryRepository repository;

    public SprintAssignmentHistoryServiceImpl(SprintAssignmentHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public SprintAssignmentHistoryEntity save(SprintAssignmentHistoryEntity entity) {
        return repository.save(entity);
    }

    @Override
    public SprintAssignmentHistoryEntity update(Long id, SprintAssignmentHistoryEntity entity) {
        SprintAssignmentHistoryEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintAssignmentHistoryEntity not found with id: " + id));
        BeanUtils.copyProperties(entity, existing, "id");
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SprintAssignmentHistoryEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintAssignmentHistoryEntity not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintAssignmentHistoryEntity> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SprintAssignmentHistoryEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
