package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.entity.admin.UserReportGroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserReportGroupService {
    Page<UserReportGroupEntity> findAll(Pageable page);
    Optional<UserReportGroupEntity> findById(Long id);
    ResponseEntity<?> save(UserReportGroupEntity entity);
    ResponseEntity<?> saveAll(List<UserReportGroupEntity> entity);
    ResponseEntity<?> update(UserReportGroupEntity entity);
    ResponseEntity<?> deleteById(Long id);
}
