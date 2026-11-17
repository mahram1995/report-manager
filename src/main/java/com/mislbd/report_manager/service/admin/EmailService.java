package com.mislbd.report_manager.service.admin;

import org.springframework.stereotype.Service;

public interface EmailService {

    void sendEmail(
            String to,
            String subject,
            String body
    );
}