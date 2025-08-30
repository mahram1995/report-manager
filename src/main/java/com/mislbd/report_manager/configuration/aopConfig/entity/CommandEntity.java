package com.mislbd.report_manager.configuration.aopConfig.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "CMD_COMMAND")
public class CommandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commandCode;
    private String commandName;
    private String moduleName;
    private String activityName;
    private Boolean isVisibleInUI;
    private Boolean isApprovalFlowRequired;
    private String approvalFlowLayer;
    private String approvalFlowProfile;
    private LocalDateTime startExecutionTime;
    private LocalDateTime endExecutionTime;
}
