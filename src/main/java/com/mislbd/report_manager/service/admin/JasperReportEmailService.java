package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.entity.admin.JasperReportSchedulerEntity;
import jakarta.mail.MessagingException;

public interface JasperReportEmailService {
    public boolean sendReport(
            String to,
            String cc,
            boolean sendSeparately,
            String subject,
            String body,
            String signature,
            String fileName,
            byte[] reportData
    ) throws Exception;
}
