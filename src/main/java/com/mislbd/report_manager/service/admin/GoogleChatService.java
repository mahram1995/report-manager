package com.mislbd.report_manager.service.admin;

import org.springframework.stereotype.Service;

@Service
public interface GoogleChatService {
    void sendMessage(String message);
}
