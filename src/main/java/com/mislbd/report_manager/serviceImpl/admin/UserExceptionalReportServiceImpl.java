package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.configuration.aopConfig.entity.ApiResponse;
import com.mislbd.report_manager.entity.admin.UserExceptionalReportEntity;
import com.mislbd.report_manager.repository.admin.UserExceptionalReportRepository;
import com.mislbd.report_manager.service.admin.UserExceptionalReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public class UserExceptionalReportServiceImpl implements UserExceptionalReportService {
    private  final UserExceptionalReportRepository repository;

    public UserExceptionalReportServiceImpl(UserExceptionalReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<UserExceptionalReportEntity> findAll(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    public Optional<UserExceptionalReportEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ResponseEntity<?> save(UserExceptionalReportEntity entity) {
        repository.save(entity);
        return ResponseEntity.ok(new ApiResponse<>("Save successfully", true, entity));

    }

    @Override
    public ResponseEntity<?> saveAll(List<UserExceptionalReportEntity> entity) {
        repository.saveAll(entity);
        return ResponseEntity.ok(new ApiResponse<>("Save successfully", true, null));

    }

    @Override
    public ResponseEntity<?> update(UserExceptionalReportEntity entity) {
        repository.save(entity);
        return ResponseEntity.ok(new ApiResponse<>("Update successfully", true, entity));

    }

    @Override
    public ResponseEntity<?> deleteById(Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>("Delete successfully", true, null));

    }
}
