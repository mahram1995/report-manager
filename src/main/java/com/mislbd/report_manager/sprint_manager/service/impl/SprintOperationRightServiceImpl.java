package com.mislbd.report_manager.sprint_manager.service.impl;

import com.mislbd.report_manager.sprint_manager.entity.SprintOperationRightEntity;
import com.mislbd.report_manager.sprint_manager.service.SprintOperationRightService;
import com.mislbd.report_manager.sprint_manager.repository.SprintOperationRightRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SprintOperationRightServiceImpl implements SprintOperationRightService {
    private final SprintOperationRightRepository repository;

    public SprintOperationRightServiceImpl(SprintOperationRightRepository repository) {
        this.repository = repository;
    }

    @Override
    public SprintOperationRightEntity save(SprintOperationRightEntity entity) {
        return repository.save(entity);
    }

    @Override
    public SprintOperationRightEntity update(Long id, SprintOperationRightEntity entity) {
        SprintOperationRightEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintOperationRightEntity not found with id: " + id));
        BeanUtils.copyProperties(entity, existing, "id");
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SprintOperationRightEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintOperationRightEntity not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintOperationRightEntity> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SprintOperationRightEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
