package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.domain.admin.JasperReportSchedulerDomain;
import com.mislbd.report_manager.entity.admin.JasperReportSchedulerEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface JasperReportSchedulerService {

    ResponseEntity<Object> saveSchedule(JasperReportSchedulerEntity entity);
    public void executeDueReports();
    ResponseEntity<String> checkReportSchedule(Long scheduleId);
    byte[]  executeReport(JasperReportSchedulerEntity schedule);

    JasperReportSchedulerDomain save(JasperReportSchedulerDomain domain);
    JasperReportSchedulerDomain update(Long id, JasperReportSchedulerDomain domain);
    JasperReportSchedulerDomain getById(Long id);
    List<JasperReportSchedulerDomain> getAll();
    void delete(Long id);
    JasperReportSchedulerDomain activate(Long id);
    JasperReportSchedulerDomain deactivate(Long id);
}
