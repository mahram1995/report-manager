package com.mislbd.report_manager.entity.admin;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "JASPER_REPORT_SCHEDULER_EXECUTION_LOG")
public class JasperReportScheduleExecutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long schedulerId;

    private String reportFileName;

    private LocalDateTime executionStartTime;

    private LocalDateTime executionEndTime;

    private Long executionDurationMs;

    private String executionStatus;

    @Lob
    private String errorMessage;

    private Boolean emailSent;

    private String emailSentTo;

    private String reportFormat;
}
