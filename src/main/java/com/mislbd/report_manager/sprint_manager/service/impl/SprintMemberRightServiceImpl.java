package com.mislbd.report_manager.sprint_manager.service.impl;

import com.mislbd.report_manager.sprint_manager.entity.SprintMemberRightEntity;
import com.mislbd.report_manager.sprint_manager.service.SprintMemberRightService;
import com.mislbd.report_manager.sprint_manager.repository.SprintMemberRightRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SprintMemberRightServiceImpl implements SprintMemberRightService {
    private final SprintMemberRightRepository repository;

    public SprintMemberRightServiceImpl(SprintMemberRightRepository repository) {
        this.repository = repository;
    }

    @Override
    public SprintMemberRightEntity save(SprintMemberRightEntity entity) {
        return repository.save(entity);
    }

    @Override
    public SprintMemberRightEntity update(Long id, SprintMemberRightEntity entity) {
        SprintMemberRightEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintMemberRightEntity not found with id: " + id));
        BeanUtils.copyProperties(entity, existing, "id");
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SprintMemberRightEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintMemberRightEntity not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintMemberRightEntity> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SprintMemberRightEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
