package com.mislbd.report_manager.domain.admin;

import lombok.Data;

import java.util.Map;

@Data
public class JasperReportRequestDomain {
    private  String jasperFileName;
    private String reportFormat;
    private Map<String, Object> parameters;
}
