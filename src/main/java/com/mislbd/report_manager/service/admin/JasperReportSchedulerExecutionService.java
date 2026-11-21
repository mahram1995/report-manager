package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.domain.admin.JasperReportRequestDomain;
import com.mislbd.report_manager.entity.admin.JasperReportSchedulerEntity;


public interface JasperReportSchedulerExecutionService {


     byte[] generateReport(JasperReportSchedulerEntity scheduler);
}
