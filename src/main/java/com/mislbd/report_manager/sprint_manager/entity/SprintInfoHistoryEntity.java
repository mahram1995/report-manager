package com.mislbd.report_manager.sprint_manager.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SPRINT_PLAN_INFO_HIST")
public class SprintInfoHistoryEntity {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sprint_entity_history_seq")
    @SequenceGenerator(name = "sprint_entity_history_seq", sequenceName = "SPRINT_ENTITY_HISTORY_SEQ", allocationSize = 1)
    private Long sprintHistId;
    private String sprintName;
    private String sprintGoal;
    private Integer sprintDuration;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate reviewDate;
    private LocalDate nextPlanningDate;
    private Long teamId;
    private String status;
}
