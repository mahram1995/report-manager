package com.mislbd.report_manager.entity.admin;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Clob;
import java.util.Date;

@Data
@Entity
@Table(name = "JASPER_REPORT_AUDIT_LOG")
public class JasperReportAuditLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reportFileName;
    private String dataSource ;
    private String reportName;
    private String reportFormat;
    private String status;
    private byte[] reportData;
    private Clob error;
    private Clob parameter;
    private String entryUser;
    private Date entryDate;
    private Date startTime;
    private Date endTime;
    private long durationInms;
    private String terminal;
    private String userClient;

}
