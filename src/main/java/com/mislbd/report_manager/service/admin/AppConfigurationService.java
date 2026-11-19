package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.entity.admin.AppConfigurationEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AppConfigurationService {
    public String getAppConfigurationValue(String itemName);
    public ResponseEntity<AppConfigurationEntity> save(AppConfigurationEntity entity);
    public ResponseEntity<AppConfigurationEntity> update(AppConfigurationEntity entity);
    public ResponseEntity<List<AppConfigurationEntity>> getAppConfiguration();
}
