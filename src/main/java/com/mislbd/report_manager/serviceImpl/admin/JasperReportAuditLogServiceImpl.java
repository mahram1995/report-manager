package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.domain.admin.JasperReportAuditDomain;
import com.mislbd.report_manager.entity.admin.JasperReportAuditLogEntity;
import com.mislbd.report_manager.mapper.admin.JasperReportAuditLogMapper;
import com.mislbd.report_manager.repository.admin.JasperReportAuditLogRepo;
import com.mislbd.report_manager.service.admin.JasperReportAuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.sql.Clob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JasperReportAuditLogServiceImpl implements JasperReportAuditLogService {

    private  final JasperReportAuditLogRepo jasperReportErrorLogRepo;
    private final JasperReportAuditLogMapper mapper;

    public JasperReportAuditLogServiceImpl(JasperReportAuditLogRepo jasperReportErrorLogRepo, JasperReportAuditLogMapper mapper) {
        this.jasperReportErrorLogRepo = jasperReportErrorLogRepo;
        this.mapper = mapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Long saveErrorLog(JasperReportAuditLogEntity entity) {
        return jasperReportErrorLogRepo.save(entity).getId();
    }

    @Override
    public JasperReportAuditLogEntity getAuditLogById(Long auditLogId) {
        return jasperReportErrorLogRepo.getAuditLogById(auditLogId);
    }

    @Override
    public ResponseEntity<Object> getErrorLog(
            String status,
            String entryUser,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            String reportName,
            Pageable pageable) {

        if (pageable != null) {

            Page<JasperReportAuditLogEntity> page =
                    jasperReportErrorLogRepo.getAuditLog(status, entryUser, fromDate, toDate, reportName, pageable);

            Page<JasperReportAuditDomain> result = page.map(mapper::entityToDomain);

            return ResponseEntity.ok(result);
        }

        List<JasperReportAuditLogEntity> list = jasperReportErrorLogRepo.getAuditLog( status, entryUser, fromDate, toDate, reportName);

        List<JasperReportAuditDomain> result = list.stream().map(mapper::entityToDomain).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<byte[]> getReport(Long auditLogId) {

            JasperReportAuditLogEntity entity = jasperReportErrorLogRepo
                    .findById(auditLogId)
                    .orElseThrow(() -> new RuntimeException("Audit log not found"));


            byte[] reportData = entity.getReportData();

            String fileName = entity.getReportFileName().split("\\.")[0]+"."+ entity.getReportFormat();

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.attachment()
                                    .filename(fileName, StandardCharsets.UTF_8)
                                    .build()
                                    .toString()
                    )
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(reportData.length)
                    .body(reportData);

    };

    @Override
    public ResponseEntity<String> getError(Long auditLogId) {

        JasperReportAuditLogEntity entity = jasperReportErrorLogRepo
                .findById(auditLogId)
                .orElseThrow(() -> new RuntimeException("Audit log not found"));

        Clob error = entity.getError();

        String errorMessage = "";

        try {
            if (error != null) {
                errorMessage = error.getSubString(1, (int) error.length());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read error log", e);
        }

        String reportName = entity.getReportFileName() != null
                ? entity.getReportFileName().split("\\.")[0]
                : "error_log";

        String fileName = reportName + "_ERROR.txt";

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(fileName, StandardCharsets.UTF_8)
                                .build()
                                .toString()
                )
                .contentType(MediaType.TEXT_PLAIN)
                .body(errorMessage);
    }
}
