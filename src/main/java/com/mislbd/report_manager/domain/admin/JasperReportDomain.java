package com.mislbd.report_manager.domain.admin;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class JasperReportDomain {

    private Long id;

    private String reportFileName;
    private String dataSource;

    private byte[] reportContent;
}
