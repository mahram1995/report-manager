package com.mislbd.report_manager.repository.admin;

import com.mislbd.report_manager.entity.admin.JasperReportAuditLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface JasperReportAuditLogRepo extends JpaRepository<JasperReportAuditLogEntity, Long> {

    @Query("SELECT j FROM JasperReportAuditLogEntity j " +
            "WHERE (:status IS NULL OR j.status = :status) " +
            "AND (:entryUser IS NULL OR LOWER(j.entryUser) LIKE LOWER(CONCAT('%', :entryUser, '%'))) " +
            "AND (:fromDate IS NULL OR j.entryDate >= :fromDate) " +
            "AND (:toDate IS NULL OR j.entryDate < :toDate) " +
            "AND (:reportName IS NULL OR LOWER(j.reportName) LIKE LOWER(CONCAT('%', :reportName, '%')))")
    Page<JasperReportAuditLogEntity> getAuditLog(
            @Param("status") String status,
            @Param("entryUser") String entryUser,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("reportName") String reportName,
            Pageable pageable
    );

    @Query("SELECT j FROM JasperReportAuditLogEntity j " +
            "WHERE (:status IS NULL OR j.status = :status) " +
            "AND (:entryUser IS NULL OR LOWER(j.entryUser) LIKE LOWER(CONCAT('%', :entryUser, '%'))) " +
            "AND (:fromDate IS NULL OR j.entryDate >= :fromDate) " +
            "AND (:toDate IS NULL OR j.entryDate < :toDate) " +
            "AND (:reportName IS NULL OR LOWER(j.reportName) LIKE LOWER(CONCAT('%', :reportName, '%'))) " +
            "ORDER BY j.id DESC")
    List<JasperReportAuditLogEntity> getAuditLog(
            @Param("status") String status,
            @Param("entryUser") String entryUser,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("reportName") String reportName
    );

    @Query("SELECT j FROM JasperReportAuditLogEntity j WHERE j.id = :auditLogId")
    JasperReportAuditLogEntity getAuditLogById(@Param("auditLogId") Long auditLogId);
}
