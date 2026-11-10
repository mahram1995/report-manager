package com.mislbd.report_manager.sprint_manager.service.impl;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskAssigneeEntity;
import com.mislbd.report_manager.sprint_manager.service.SprintTaskAssigneeService;
import com.mislbd.report_manager.sprint_manager.repository.SprintTaskAssigneeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SprintTaskAssigneeServiceImpl implements SprintTaskAssigneeService {
    private final SprintTaskAssigneeRepository repository;

    public SprintTaskAssigneeServiceImpl(SprintTaskAssigneeRepository repository) {
        this.repository = repository;
    }

    @Override
    public SprintTaskAssigneeEntity save(SprintTaskAssigneeEntity entity) {
        return repository.save(entity);
    }

    @Override
    public SprintTaskAssigneeEntity update(Long id, SprintTaskAssigneeEntity entity) {
        SprintTaskAssigneeEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTaskAssigneeEntity not found with id: " + id));
        BeanUtils.copyProperties(entity, existing, "id");
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SprintTaskAssigneeEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTaskAssigneeEntity not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintTaskAssigneeEntity> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SprintTaskAssigneeEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
