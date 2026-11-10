package com.mislbd.report_manager.controller.admin;
import com.mislbd.report_manager.entity.admin.JasperReportAuditLogEntity;

import com.mislbd.report_manager.service.admin.JasperReportAuditLogService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Clob;
import java.util.Date;

@RestController
@RequestMapping("/admin/jasper-reports-audit")
@RequiredArgsConstructor
public class JasperReportAuditController {

    private final JasperReportAuditLogService auditLogService;

    /**
     * Save Error Log
     */
    @PostMapping("/save")
    public ResponseEntity<Long> saveErrorLog(
            @RequestBody JasperReportAuditLogEntity entity) {

        return ResponseEntity.ok(
                auditLogService.saveErrorLog(entity)
        );
    }

    /**
     * Get Error Logs (List or Paginated)
     */
    @GetMapping("/get-audit-log")
    public ResponseEntity<Object> getErrorLog(
            @RequestParam(name = "status",required = false) String status,
            @RequestParam(name = "entryUser", required = false) String entryUser,

            @RequestParam(name = "fromDate",required = false)
            @DateTimeFormat( pattern = "yyyy-MM-dd") Date fromDate,

            @RequestParam(name = "toDate", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,

            @RequestParam(name = "reportName",required = false) String reportName,
            @RequestParam(name = "asList",defaultValue = "true") boolean asList,
            @RequestParam(name = "page",required = false) Integer page,
            @RequestParam(name = "size",required = false) Integer size
    ) {

        Pageable pageable = null;
        if(asList){
            if (page != null && size != null) {
                pageable = PageRequest.of(
                        page,
                        size,
                        Sort.by(Sort.Direction.DESC, "id")
                );
            }
        }


        return auditLogService.getErrorLog(
                status,
                entryUser,
                fromDate,
                toDate,
                reportName,
                pageable
        );
    }

    /**
     * Get Generated Report By Audit Log ID
     */
    @GetMapping("/get-report")
    public ResponseEntity<byte[]> getReport(
            @RequestParam(name = "auditLogId",required = true) Long auditLogId) {

        return auditLogService.getReport(auditLogId);
    }

    /**
     * Get Error Details By Audit Log ID
     */
    @GetMapping("/get-error")
    public ResponseEntity<String> getError(
            @RequestParam(name = "auditLogId",required = true) Long auditLogId) {

        return auditLogService.getError(auditLogId);
    }
}
