package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.domain.admin.JasperReportDomain;
import com.mislbd.report_manager.domain.admin.JasperReportRequestDomain;
import com.mislbd.report_manager.projection.JasperReportHistAuditProjection;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface JasperReportService {
    JasperReportDomain save(JasperReportDomain domain);
    List<JasperReportHistAuditProjection> getAuditList(String reportFileName);
    JasperReportDomain update(Long id, JasperReportDomain domain);

    JasperReportDomain getByReportFileName(String reportFileName);
    List<JasperReportDomain> getJasperReports();
    ResponseEntity<byte[]> getJasperReportContent(Long id);
    ResponseEntity<byte[]> getJasperReportHistContent(Long histId);

    ResponseEntity<byte[]> runReport(JasperReportRequestDomain request) throws IOException, JRException, SQLException;
}
