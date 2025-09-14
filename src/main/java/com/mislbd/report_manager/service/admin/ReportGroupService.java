package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.entity.admin.ReportGroupEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ReportGroupService {
    List<ReportGroupEntity> findAllReportGroup();
    Optional<ReportGroupEntity> findById(Long id);
    ResponseEntity<?> saveReportGroup(ReportGroupEntity data);
    ResponseEntity<?> updateReportGroup(ReportGroupEntity data);

}
