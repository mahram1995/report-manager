package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.entity.admin.JasperReportSchedulerEntity;

import java.time.LocalDateTime;

public interface JasperReportSchedulerHistoryService {
  void   saveFailure(
            JasperReportSchedulerEntity schedule,
            LocalDateTime startTime,
            LocalDateTime date,
            Exception ex
            );

  void saveSuccess(
          JasperReportSchedulerEntity schedule,
          LocalDateTime startTime,
          LocalDateTime date,
          Integer length,
          String emailTo,
          String emailCC,
          Boolean isEmailSet
            );
}
