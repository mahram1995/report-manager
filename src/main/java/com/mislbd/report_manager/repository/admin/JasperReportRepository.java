package com.mislbd.report_manager.repository.admin;

import com.mislbd.report_manager.entity.admin.JasperReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JasperReportRepository extends JpaRepository<JasperReportEntity, Long> {
    boolean existsByReportFileName(String reportFileName);

    Optional<JasperReportEntity> findByReportFileName(String reportFileName);
}
