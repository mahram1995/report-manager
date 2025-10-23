package com.mislbd.report_manager.configuration.aopConfig.repository;

import com.mislbd.report_manager.configuration.aopConfig.entity.CommandMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandMetadataRepository extends JpaRepository<CommandMetadataEntity, Long> {
    boolean existsByPackageNameAndClassName(String packageName, String className);

}