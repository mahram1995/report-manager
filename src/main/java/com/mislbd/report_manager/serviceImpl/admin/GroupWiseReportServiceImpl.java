package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.configuration.aopConfig.entity.ApiResponse;
import com.mislbd.report_manager.entity.admin.GroupWiseReportEntity;
import com.mislbd.report_manager.repository.admin.GroupWiseReportRepository;
import com.mislbd.report_manager.service.admin.GroupWiseReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public class GroupWiseReportServiceImpl implements GroupWiseReportService {
    private final GroupWiseReportRepository repository;

    public GroupWiseReportServiceImpl(GroupWiseReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<GroupWiseReportEntity> findAll(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    public Optional<GroupWiseReportEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ResponseEntity<?> save(GroupWiseReportEntity entity) {
        repository.save(entity);
        return ResponseEntity.ok(new ApiResponse<>("Save successfully", true, entity));

    }

    @Override
    public ResponseEntity<?> saveAll(List<GroupWiseReportEntity> entity) {
        repository.saveAll(entity);
        return ResponseEntity.ok(new ApiResponse<>("Delete successfully", true, null));

    }

    @Override
    public ResponseEntity<?> update(GroupWiseReportEntity entity) {
        repository.save(entity);
        return ResponseEntity.ok(new ApiResponse<>("Update successfully", true, entity));

    }

    @Override
    public ResponseEntity<?> deleteById(Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>("Delete successfully", true, null));
    }
}
