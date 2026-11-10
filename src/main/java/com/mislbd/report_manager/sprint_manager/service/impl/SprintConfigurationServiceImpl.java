package com.mislbd.report_manager.sprint_manager.service.impl;

import com.mislbd.report_manager.sprint_manager.entity.SprintConfigurationEntity;
import com.mislbd.report_manager.sprint_manager.service.SprintConfigurationService;
import com.mislbd.report_manager.sprint_manager.repository.SprintConfigurationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SprintConfigurationServiceImpl implements SprintConfigurationService {
    private final SprintConfigurationRepository repository;

    public SprintConfigurationServiceImpl(SprintConfigurationRepository repository) {
        this.repository = repository;
    }

    @Override
    public SprintConfigurationEntity save(SprintConfigurationEntity entity) {
        return repository.save(entity);
    }

    @Override
    public SprintConfigurationEntity update(Long id, SprintConfigurationEntity entity) {
        SprintConfigurationEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintConfigurationEntity not found with id: " + id));
        BeanUtils.copyProperties(entity, existing, "id");
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SprintConfigurationEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintConfigurationEntity not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintConfigurationEntity> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SprintConfigurationEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
