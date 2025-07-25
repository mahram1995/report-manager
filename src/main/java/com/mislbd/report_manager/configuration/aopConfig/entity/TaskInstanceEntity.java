package com.mislbd.report_manager.configuration.aopConfig.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.java.Log;

import java.sql.Clob;
import java.sql.Date;
@Entity(name = "AF_TASK_INSTANCE")
@Data
public class TaskInstanceEntity {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_generator")
    @SequenceGenerator(
            name = "task_id_generator",
            allocationSize = 1,
            sequenceName = "task_id_sequence")
    private Long taskId;
    @Lob
    private String payload;
    String  activityName;
    String  verifierBranchId;
    String  verifierDepartmentId;
    String  commandName;
    String  taskCorrectionUi;
    String  taskDetailsUi;
    String  domainReference;
    String  maker;
    String  verifier;
    String  makerBranchId;
    String  status;
    String  verifierTerminal;
    String  makerTerminal;
}
