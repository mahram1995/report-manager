package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.entity.admin.AppConfigurationEntity;
import com.mislbd.report_manager.repository.admin.AppConfigurationRepository;
import com.mislbd.report_manager.service.admin.AppConfigurationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppConfigurationServiceImpl implements AppConfigurationService {

    private final AppConfigurationRepository repository;

    public AppConfigurationServiceImpl(AppConfigurationRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getAppConfigurationValue(String itemName) {
        return repository.findByName(itemName).getActualValue();
    }

    @Override
    public ResponseEntity<AppConfigurationEntity> save(AppConfigurationEntity entity) {
        if(entity.getName().isEmpty()){
            throw new RuntimeException("Name Can't be Null");
        }
        if(entity.getDescription().isEmpty()){
            throw new RuntimeException("Description Can't be Null");
        }
        if(entity.getExpectedValue().isEmpty()){
            throw new RuntimeException("Excepted Value Can't be Null");
        }
        return ResponseEntity.ok( repository.save(entity));
    }

    @Override
    public ResponseEntity<AppConfigurationEntity> update(AppConfigurationEntity entity) {
        if(entity.getActualValue().isEmpty()){
            throw new RuntimeException("Actual Value Can't be Null");
        }  if(entity.getDescription().isEmpty()){
            throw new RuntimeException("Description Can't be Null");
        }
        if(entity.getExpectedValue().isEmpty()){
            throw new RuntimeException("Excepted Value Can't be Null");
        }

        return ResponseEntity.ok( repository.save(entity));
    }

    @Override
    public ResponseEntity<List<AppConfigurationEntity>> getAppConfiguration(AppConfigurationEntity entity) {
        return ResponseEntity.ok(repository.findAll());
    }
}
