package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.configuration.aopConfig.domain.CommandResponse;
import com.mislbd.report_manager.configuration.aopConfig.entity.ApiResponse;
import com.mislbd.report_manager.entity.admin.ReportGroupEntity;
import com.mislbd.report_manager.repository.admin.ReportGroupRepository;
import com.mislbd.report_manager.service.admin.ReportGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
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
    public CommandResponse<?> saveReportGroup(ReportGroupEntity data) {
        return new CommandResponse<>(repository.save(data));

    }

    @Override
    public  CommandResponse<?> updateReportGroup(ReportGroupEntity data) {
        return new CommandResponse<>(repository.save(data));
    }
}
