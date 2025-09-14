package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.configuration.aopConfig.entity.ApiResponse;
import com.mislbd.report_manager.entity.admin.UserReportGroupEntity;
import com.mislbd.report_manager.repository.admin.UserReportGroupRepository;
import com.mislbd.report_manager.service.admin.UserReportGroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public class UserReportGroupServiceImpl implements UserReportGroupService {
    private  final UserReportGroupRepository repository;

    public UserReportGroupServiceImpl(UserReportGroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<UserReportGroupEntity> findAll(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    public Optional<UserReportGroupEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ResponseEntity<?> save(UserReportGroupEntity entity) {
        repository.save(entity);
        return ResponseEntity.ok(new ApiResponse<>("User group save successfully", true, entity));

    }

    @Override
    public ResponseEntity<?> saveAll(List<UserReportGroupEntity> entity) {
        repository.saveAll(entity);
        return ResponseEntity.ok(new ApiResponse<>("Save successfully", true, null));

    }

    @Override
    public ResponseEntity<?> update(UserReportGroupEntity entity) {
        repository.save(entity);
        return ResponseEntity.ok(new ApiResponse<>("Update successfully", true, entity));

    }

    @Override
    public ResponseEntity<?> deleteById(Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>("Delete successfully", true, null));
    }
}
