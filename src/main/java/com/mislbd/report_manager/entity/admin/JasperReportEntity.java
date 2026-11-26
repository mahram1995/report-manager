package com.mislbd.report_manager.entity.admin;

import com.mislbd.report_manager.configuration.aopConfig.auditListener.AuditEntityListener;
import com.mislbd.report_manager.configuration.aopConfig.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Clob;
import java.util.Date;

@Data
@Entity
@Table(name = "jasper_report")
@EntityListeners(AuditEntityListener.class)
public class JasperReportEntity extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "REPORT_FILE_NAME", nullable = false, unique = true, length = 255)
    private String reportFileName;
    private String dataSource ;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "REPORT_CONTENT", nullable = false)
    private byte[] reportContent;
    private Integer executionTimeoutSeconds;
    private boolean isQrcodeEnable;
    private Clob sqlQueryForQrCode;
    private Integer maxQrcodeScanLimit;
    private Integer daysOfQrCodeExpiry;
}

