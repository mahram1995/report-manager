package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.domain.admin.JasperReportDomain;
import com.mislbd.report_manager.domain.admin.JasperReportRequestDomain;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface JasperReportService {
    JasperReportDomain save(JasperReportDomain domain);

    JasperReportDomain update(Long id, JasperReportDomain domain);

    JasperReportDomain getByReportFileName(String reportFileName);
    List<JasperReportDomain> getByJasperReposts();

    ResponseEntity<byte[]> runReport(JasperReportRequestDomain request) throws IOException, JRException;
}
