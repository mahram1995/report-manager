package com.mislbd.report_manager.sprint_manager.service.impl;

import com.mislbd.report_manager.sprint_manager.entity.SprintTeamConfigurationEntity;
import com.mislbd.report_manager.sprint_manager.repository.SprintTeamConfigurationRepository;
import com.mislbd.report_manager.sprint_manager.service.SprintTeamConfigurationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SprintTeamConfigurationServiceImpl implements SprintTeamConfigurationService {
    private final SprintTeamConfigurationRepository repository;

    public SprintTeamConfigurationServiceImpl(SprintTeamConfigurationRepository repository) {
        this.repository = repository;
    }

    @Override
    public SprintTeamConfigurationEntity save(SprintTeamConfigurationEntity entity) {
        return repository.save(entity);
    }

    @Override
    public SprintTeamConfigurationEntity update(Long id, SprintTeamConfigurationEntity entity) {
        SprintTeamConfigurationEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTeamConfigurationEntity not found with id: " + id));
        BeanUtils.copyProperties(entity, existing, "id");
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SprintTeamConfigurationEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTeamConfigurationEntity not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintTeamConfigurationEntity> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SprintTeamConfigurationEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
