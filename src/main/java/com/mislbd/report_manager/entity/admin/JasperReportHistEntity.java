package com.mislbd.report_manager.entity.admin;

import com.mislbd.report_manager.configuration.aopConfig.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Blob;

@Getter
@Setter
@Entity
@Table(name = "jasper_report_hist")
public class JasperReportHistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "REPORT_FILE_NAME", nullable = false, length = 255)
    private String reportFileName;
    private String dataSource ;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "REPORT_CONTENT", nullable = false)
    private byte[] reportContent;
}

