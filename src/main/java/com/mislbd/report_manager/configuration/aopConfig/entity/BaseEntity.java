package com.mislbd.report_manager.configuration.aopConfig.entity;

import com.mislbd.report_manager.configuration.aopConfig.listener.AuditEntityListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@MappedSuperclass
@Setter
@Getter
@EntityListeners(AuditEntityListener.class)
public class BaseEntity {

    private String createdBy;
    private LocalDate createDate;
    private String createAgent;
    private String createdTerminal;

    private String verifyBy;
    private LocalDate verifyDate;
    private String verifyAgent;
    private String verifyTerminal;

    private String updateBy;
    private LocalDate updateDate;
    private String updateAgent;
    private String updateTerminal;

    // Getters & Setters
}
