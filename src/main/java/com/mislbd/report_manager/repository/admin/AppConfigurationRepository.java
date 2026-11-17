package com.mislbd.report_manager.repository.admin;

import com.mislbd.report_manager.entity.admin.AppConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppConfigurationRepository extends JpaRepository<AppConfigurationEntity, Long> {
    AppConfigurationEntity findByName(String itemName);
}
