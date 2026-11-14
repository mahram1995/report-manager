package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.entity.admin.JasperReportAuditLogEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public interface JasperReportAuditLogService {
    Long saveErrorLog(JasperReportAuditLogEntity entity);
    JasperReportAuditLogEntity getAuditLogById(Long auditLogId);
    ResponseEntity<Object> getErrorLog(String status, String entryUser, LocalDateTime fromDate, LocalDateTime toDate, String reportName, Pageable pagebale);
    ResponseEntity<byte[]> getReport(Long auditLogId);
    ResponseEntity<String> getError(Long auditLogId);;
}
