package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.configuration.aopConfig.entity.ApiResponse;
import com.mislbd.report_manager.entity.admin.ReportGroupEntity;
import com.mislbd.report_manager.repository.admin.ReportGroupRepository;
import com.mislbd.report_manager.service.admin.ReportGroupService;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public class ReportGroupServiceImpl implements ReportGroupService {
    private final ReportGroupRepository repository;

    public ReportGroupServiceImpl(ReportGroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ReportGroupEntity> findAllReportGroup() {
        return repository.findAll();
    }

    @Override
    public Optional<ReportGroupEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ResponseEntity<?> saveReportGroup(ReportGroupEntity data) {
        repository.save(data);
        return ResponseEntity.ok(new ApiResponse<>("Dave successfully", true, data));

    }

    @Override
    public ResponseEntity<?> updateReportGroup(ReportGroupEntity data) {
        repository.save(data);
        return ResponseEntity.ok(new ApiResponse<>("Update successfully", true, null));

    }
}
