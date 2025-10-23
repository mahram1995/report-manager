package com.mislbd.report_manager.configuration.aopConfig.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "command",
        uniqueConstraints = @UniqueConstraint(columnNames = {"packageName", "className"})
)
public class CommandMetadataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String className;
    private String packageName;
    private String name;
    private String description;
    private String module;

    // Getters and setters
}