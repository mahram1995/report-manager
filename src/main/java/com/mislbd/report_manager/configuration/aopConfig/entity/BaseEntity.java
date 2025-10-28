package com.mislbd.report_manager.configuration.aopConfig.entity;

import com.mislbd.report_manager.configuration.aopConfig.auditListener.AuditEntityListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
@Setter
@Getter
@EntityListeners(AuditEntityListener.class)
public class BaseEntity {
    @Column(updatable = false)
    private String createdBy;
    @Column(updatable = false)
    private LocalDate createDate;
    @Column(updatable = false)
    private LocalDateTime createDateTime;
    @Column(updatable = false)
    private String createAgent;
    @Column(updatable = false)
    private String createdTerminal;

    private String verifyBy;
    private LocalDate verifyDate;
    private LocalDateTime verifyDateTime;
    private String verifyAgent;
    private String verifyTerminal;

    private String updateBy;
    private LocalDate updateDate;
    private LocalDateTime updateDateTime;
    private String updateAgent;
    private String updateTerminal;

    // Getters & Setters
}
