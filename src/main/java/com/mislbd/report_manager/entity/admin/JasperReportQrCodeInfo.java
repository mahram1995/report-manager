package com.mislbd.report_manager.entity.admin;

import com.mislbd.report_manager.configuration.aopConfig.auditListener.AuditEntityListener;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Clob;
import java.util.Date;

@Data
@Entity
@Table(name = "jasper_report_qrcode_info")
@EntityListeners(AuditEntityListener.class)
public class JasperReportQrCodeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Clob value;
    private Integer numberOfScan;
    private Integer scanLimit;
    private String securityToken;
    private Date expiryDate;
    private Clob parameters;
    private Clob sqlQueryForQrCode;
    private String uuid;
    private String reportFileName;
    private Long auditLogId;
}
