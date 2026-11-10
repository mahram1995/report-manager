package com.mislbd.report_manager.sprint_manager.service.impl;

import com.mislbd.report_manager.sprint_manager.entity.SprintTaskPointEntity;
import com.mislbd.report_manager.sprint_manager.repository.SprintTaskPointRepository;
import com.mislbd.report_manager.sprint_manager.service.SprintTaskPointService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SprintTaskPointServiceImpl implements SprintTaskPointService {
    private final SprintTaskPointRepository repository;

    public SprintTaskPointServiceImpl(SprintTaskPointRepository repository) {
        this.repository = repository;
    }

    @Override
    public SprintTaskPointEntity save(SprintTaskPointEntity entity) {
        return repository.save(entity);
    }

    @Override
    public SprintTaskPointEntity update(Long id, SprintTaskPointEntity entity) {
        SprintTaskPointEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTaskPointEntity not found with id: " + id));
        BeanUtils.copyProperties(entity, existing, "id");
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SprintTaskPointEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTaskPointEntity not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintTaskPointEntity> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SprintTaskPointEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
