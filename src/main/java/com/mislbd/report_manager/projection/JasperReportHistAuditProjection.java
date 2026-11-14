package com.mislbd.report_manager.projection;

import java.time.LocalDateTime;
import java.util.Date;

public interface JasperReportHistAuditProjection {
    String getAuditNo();
    Long getHistId();
    String getDataSource();
    String getCreateBy();
    String getCreateDate();
    String getUpdateBy();
    String getUpdateDate();
}
