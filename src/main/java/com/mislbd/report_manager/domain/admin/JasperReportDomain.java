package com.mislbd.report_manager.domain.admin;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class JasperReportDomain {

    private Long id;

    private String reportFileName;
    private String dataSource;
    private String createDate;
    private String updateDate;
    private String verifyDate;
    private String createdBy;
    private String verifyBy;
    private String updateBy;
    private byte[] reportContent;
}
