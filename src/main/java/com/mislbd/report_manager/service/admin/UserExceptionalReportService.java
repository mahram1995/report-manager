package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.entity.admin.UserExceptionalReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserExceptionalReportService {
    Page<UserExceptionalReportEntity> findAll(Pageable page);
    Optional<UserExceptionalReportEntity> findById(Long id);
    ResponseEntity<?> save(UserExceptionalReportEntity entity);
    ResponseEntity<?> saveAll(List<UserExceptionalReportEntity> entity);
    ResponseEntity<?> update(UserExceptionalReportEntity entity);
    ResponseEntity<?> deleteById(Long id);
}
