package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.entity.admin.JasperReportEntity;
import org.springframework.http.ResponseEntity;

import javax.sql.DataSource;
import java.sql.Clob;
import java.sql.Connection;

public interface JasperQrcodeGenerationService {
    String generateQrcode(Object parameter, JasperReportEntity reportEntity, DataSource dataSource, Long reportAuditId);
    void  deletedQrcodeInfo(String securityToken);

    ResponseEntity<Object> verifyQrcode(String securityCode);
}
