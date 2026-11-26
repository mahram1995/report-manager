package com.mislbd.report_manager.repository.admin;

import com.mislbd.report_manager.entity.admin.JasperReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JasperReportRepository extends JpaRepository<JasperReportEntity, Long> {
    boolean existsByReportFileName(String reportFileName);

    Optional<JasperReportEntity> findByReportFileName(String reportFileName);

    @Query("""
        SELECT j
        FROM JasperReportEntity j
        WHERE (:isQrcodeEnable IS NULL OR j.isQrcodeEnable = :isQrcodeEnable)
          AND (:reportFileName IS NULL OR LOWER(j.reportFileName) LIKE LOWER(CONCAT('%', :reportFileName, '%')))
        ORDER BY j.id DESC
    """)
    List<JasperReportEntity> findAllJasperReports(
            @Param("isQrcodeEnable") Boolean isQrcodeEnable,
            @Param("reportFileName") String reportFileName
    );
}
