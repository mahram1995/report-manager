package com.mislbd.report_manager.projection;

public interface JasperReportHistAuditProjection {
    String getAuditNo();
    Long getHistId();
    String getDataSource();
    String getCreateBy();
    String getCreteDate();
    String getUpdateBy();
    String getUpdateDate();
}
