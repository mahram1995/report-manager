package com.mislbd.report_manager.sprint_manager.service.impl;

import com.mislbd.report_manager.sprint_manager.entity.SprintTeamMemberEntity;
import com.mislbd.report_manager.sprint_manager.repository.SprintTeamMemberRepository;
import com.mislbd.report_manager.sprint_manager.service.SprintTeamMemberService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SprintTeamMemberServiceImpl implements SprintTeamMemberService {
    private final SprintTeamMemberRepository repository;

    public SprintTeamMemberServiceImpl(SprintTeamMemberRepository repository) {
        this.repository = repository;
    }

    @Override
    public SprintTeamMemberEntity save(SprintTeamMemberEntity entity) {
        return repository.save(entity);
    }

    @Override
    public SprintTeamMemberEntity update(Long id, SprintTeamMemberEntity entity) {
        SprintTeamMemberEntity existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTeamMemberEntity not found with id: " + id));
        BeanUtils.copyProperties(entity, existing, "id");
        return repository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public SprintTeamMemberEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SprintTeamMemberEntity not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SprintTeamMemberEntity> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SprintTeamMemberEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
