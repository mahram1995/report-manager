package com.mislbd.report_manager.sprint_manager.service.impl;

import com.mislbd.report_manager.sprint_manager.entity.SprintTeamEntity;
import com.mislbd.report_manager.sprint_manager.repository.SprintTeamRepository;
import com.mislbd.report_manager.sprint_manager.service.SprintTeamService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SprintTeamServiceImpl implements SprintTeamService {
    private final SprintTeamRepository repository;

    public SprintTeamServiceImpl(SprintTeamRepository repository) {
        this.repository = repository;
    }

    @Override
    public SprintTeamEntity save(SprintTeamEntity entity) {
        return repository.save(entity);
    }

    @Override
    public SprintTeamEntity update(Long id, SprintTeamEntity entity) {
        SprintTeamEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTeamEntity not found with id: " + id));
        BeanUtils.copyProperties(entity, existing, "id");
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SprintTeamEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTeamEntity not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintTeamEntity> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SprintTeamEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
