package com.mislbd.report_manager.entity.admin;

import com.mislbd.report_manager.configuration.aopConfig.auditListener.AuditEntityListener;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Blob;

@Data
@Entity
@Table(name = "APP_CONFIGURATION")
@EntityListeners(AuditEntityListener.class)
public class AppConfigurationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String expectedValue;
    private String actualValue;
    private Boolean isChangeAble;
    private Boolean isVisible;
}
