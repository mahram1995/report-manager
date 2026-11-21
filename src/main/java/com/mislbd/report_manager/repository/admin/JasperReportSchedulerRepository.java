package com.mislbd.report_manager.repository.admin;

import com.mislbd.report_manager.entity.admin.JasperReportSchedulerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JasperReportSchedulerRepository extends JpaRepository<JasperReportSchedulerEntity, Long> {
    List<JasperReportSchedulerEntity> findByIsActiveTrueAndNextRunAtLessThanEqual(
            LocalDateTime currentDateTime
    );


    List<JasperReportSchedulerEntity>
    findByIsActiveTrueAndNextRunAtIsNull();

    @Query("""
        SELECT s FROM JasperReportSchedulerEntity s
        WHERE s.isActive = true
          AND s.nextRunAt <= :now
        ORDER BY s.nextRunAt ASC
    """)
    List<JasperReportSchedulerEntity> findDueSchedules(
            @Param("now") LocalDateTime now
    );
}
