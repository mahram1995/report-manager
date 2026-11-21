package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.domain.admin.JasperReportSchedulerDomain;
import com.mislbd.report_manager.entity.admin.JasperReportScheduleExecutionEntity;
import com.mislbd.report_manager.entity.admin.JasperReportSchedulerEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface JasperReportSchedulerService {
    public void executeDueReports();
    ResponseEntity<Object> checkReportSchedule(Long scheduleId);
    JasperReportSchedulerDomain save(JasperReportSchedulerDomain domain);
    JasperReportSchedulerDomain update(JasperReportSchedulerDomain domain);
    JasperReportSchedulerDomain getById(Long id);
    List<JasperReportSchedulerDomain> getAll();
    void delete(Long id);
    JasperReportSchedulerDomain activate(Long id);
    JasperReportSchedulerDomain deactivate(Long id);
    List<JasperReportScheduleExecutionEntity> getScheduleExecutionLog();
}
