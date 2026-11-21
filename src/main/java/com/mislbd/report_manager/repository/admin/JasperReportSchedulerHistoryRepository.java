package com.mislbd.report_manager.repository.admin;

import com.mislbd.report_manager.entity.admin.JasperReportScheduleExecutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JasperReportSchedulerHistoryRepository extends JpaRepository<JasperReportScheduleExecutionEntity, Long> {
}
