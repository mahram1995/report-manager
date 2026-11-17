package com.mislbd.report_manager.entity.admin;

import com.mislbd.report_manager.configuration.aopConfig.auditListener.AuditEntityListener;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Clob;
import java.util.Date;

@Data
@Entity
@Table(name = "jasper_report_scheduler")
@EntityListeners(AuditEntityListener.class)
public class JasperReportSchedulerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "REPORT_FILE_NAME", nullable = false, unique = true, length = 255)
    private String reportFileName;
    private String  emailAddressTo;
    private String  emailAddressCc;
    private Boolean isSendEmailSeparately;
    private String  emailSubject;
    private String  emailBody;
    private String  emailSignature;
    private String  scheduleTime;
    private String  scheduleFrequency;
    private Date scheduleDate;
    private String scheduleDay;
}
