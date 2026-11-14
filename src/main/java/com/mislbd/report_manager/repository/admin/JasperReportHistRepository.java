package com.mislbd.report_manager.repository.admin;

import com.mislbd.report_manager.entity.admin.JasperReportEntity;
import com.mislbd.report_manager.entity.admin.JasperReportHistEntity;
import com.mislbd.report_manager.projection.JasperReportHistAuditProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JasperReportHistRepository extends JpaRepository<JasperReportHistEntity, Long> {
    @Query(value = """
        SELECT
            'Commit_' || ROW_NUMBER() OVER (ORDER BY id asc) AS auditNo,
            id AS histId,
            DATA_SOURCE AS dataSource,
            created_By AS createBy,
            TO_CHAR(CREATE_DATE, 'YYYY-MM-DD HH24:MI:SS') AS createDate,
            update_by AS updateBy,
            TO_CHAR(UPDATE_DATE, 'YYYY-MM-DD HH24:MI:SS') AS updateDate
        FROM JASPER_REPORT_HIST
        WHERE report_file_name = :reportFileName
        ORDER BY id DESC
        """, nativeQuery = true)
    List<JasperReportHistAuditProjection> findAuditList(
            @Param("reportFileName") String reportFileName);
}
