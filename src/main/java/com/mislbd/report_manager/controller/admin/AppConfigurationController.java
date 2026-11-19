package com.mislbd.report_manager.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.mislbd.report_manager.entity.admin.AppConfigurationEntity;
import com.mislbd.report_manager.service.admin.AppConfigurationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/app-configuration")
@RequiredArgsConstructor
public class AppConfigurationController {

    private final AppConfigurationService appConfigurationService;

    /**
     * Get configuration value by name
     */
    @GetMapping("/value/{itemName}")
    public ResponseEntity<String> getAppConfigurationValue(
            @PathVariable String itemName) {

        String value = appConfigurationService
                .getAppConfigurationValue(itemName);

        return ResponseEntity.ok(value);
    }

    /**
     * Save application configuration
     */
    @PostMapping("/save")
    public ResponseEntity<AppConfigurationEntity> save(
            @RequestBody AppConfigurationEntity entity) {

        return appConfigurationService.save(entity);
    }

    /**
     * Update application configuration
     */
    @PutMapping("/update")
    public ResponseEntity<AppConfigurationEntity> update(
            @RequestBody AppConfigurationEntity entity) {

        return appConfigurationService.update(entity);
    }

    /**
     * Get application configurations
     */
    @GetMapping("/get")
    public ResponseEntity<List<AppConfigurationEntity>> getAppConfiguration() {

        return appConfigurationService.getAppConfiguration();
    }
}
