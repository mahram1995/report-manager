package com.mislbd.report_manager.domain.admin;

import lombok.Data;

import java.sql.Clob;
import java.util.Date;

@Data
public class JasperReportAuditDomain {
    private Long id;
    private String reportFileName;
    private String dataSource ;
    private String reportName;
    private String reportFormat;
    private String status;
    private byte[] reportData;
    private Clob error;
    private String parameter;
    private String entryUser;
    private Date entryDate;
    private Date startTime;
    private Date endTime;
    private Long durationInms;
    private String terminal;
    private String userClient;
}
