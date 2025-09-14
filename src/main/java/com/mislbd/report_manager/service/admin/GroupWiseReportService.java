package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.entity.admin.GroupWiseReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GroupWiseReportService {
    Page<GroupWiseReportEntity> findAll(Pageable page);
    Optional<GroupWiseReportEntity> findById(Long id);
    ResponseEntity<?> save(GroupWiseReportEntity entity);
    ResponseEntity<?> saveAll(List<GroupWiseReportEntity> entity);
    ResponseEntity<?> update(GroupWiseReportEntity entity);
    ResponseEntity<?> deleteById(Long id);
}
