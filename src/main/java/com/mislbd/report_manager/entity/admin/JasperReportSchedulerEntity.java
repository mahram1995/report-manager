package com.mislbd.report_manager.entity.admin;

import com.mislbd.report_manager.configuration.aopConfig.auditListener.AuditEntityListener;
import com.mislbd.report_manager.enam.ScheduleFrequency;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Clob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
@Entity
@Table(name = "jasper_report_scheduler")
@EntityListeners(AuditEntityListener.class)
public class JasperReportSchedulerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reportFileName;
    private String reportFormat;
    @Lob
    private String reportParameters;

    @Column(length = 2000)
    private String emailAddressTo;

    @Column(length = 2000)
    private String emailAddressCc;

    private Boolean isSendEmailSeparately;

    private String emailSubject;

    @Lob
    private String emailBody;

    @Lob
    private String emailSignature;

    // HH:mm
    private String scheduleTime;

    @Enumerated(EnumType.STRING)
    private ScheduleFrequency scheduleFrequency;

    // Monthly/quarterly/half-yearly/yearly execution day
    private Integer scheduleDate;

    // Weekly configuration
    // Example: "6" or "1,3,6" or "1:6,2:3,3:1,4:4"
    private String scheduleDay;

    // Anchor month for quarterly/half-yearly/yearly
    private Integer scheduleMonth;

    // Number of weeks in a repeating weekly cycle
    private Integer scheduleWeekCycle;

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean isActive;

    private LocalDateTime nextRunAt;
    private LocalDateTime lastRunAt;

    private String lastStatus;

    @Lob
    private String lastError;
}
