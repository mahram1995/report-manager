package com.mislbd.report_manager.repository.admin;

import com.mislbd.report_manager.entity.admin.JasperReportEntity;
import com.mislbd.report_manager.entity.admin.JasperReportHistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JasperReportHistRepository extends JpaRepository<JasperReportHistEntity, Long> {
}
