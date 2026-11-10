package com.mislbd.report_manager.serviceImpl.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mislbd.report_manager.service.admin.GoogleChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class GoogleChatServiceImpl implements GoogleChatService {
    private final RestClient.Builder restClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${app.google-chat.webhook-url}") // this an environmental variable which is declared in application.yml file.
    private String webhookUrl;

    public GoogleChatServiceImpl(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
        this.restClientBuilder = restClientBuilder;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendMessage(String message) {
        restClientBuilder.build()
                .post()
                .uri(webhookUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("text", message))
                .retrieve()
                .toBodilessEntity();
    }
}
