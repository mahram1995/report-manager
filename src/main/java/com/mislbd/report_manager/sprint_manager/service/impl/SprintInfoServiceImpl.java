package com.mislbd.report_manager.sprint_manager.service.impl;

import com.mislbd.report_manager.sprint_manager.entity.SprintInfoEntity;
import com.mislbd.report_manager.sprint_manager.repository.SprintInfoRepository;
import com.mislbd.report_manager.sprint_manager.service.SprintInfoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SprintInfoServiceImpl implements SprintInfoService {
    private final SprintInfoRepository repository;

    public SprintInfoServiceImpl(SprintInfoRepository repository) {
        this.repository = repository;
    }

    @Override
    public SprintInfoEntity save(SprintInfoEntity entity) {
        return repository.save(entity);
    }

    @Override
    public SprintInfoEntity update(Long id, SprintInfoEntity entity) {
        SprintInfoEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintInfoEntity not found with id: " + id));
        BeanUtils.copyProperties(entity, existing, "id");
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SprintInfoEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintInfoEntity not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintInfoEntity> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SprintInfoEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
