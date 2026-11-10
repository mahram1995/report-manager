package com.mislbd.report_manager.sprint_manager.entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="SPRINT_TASK_HISTORY")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SprintTaskHistoryEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long taskHistId;
    private String itemName;
    private String description;
    private String client;
    private String priority;
    private String module;
    private String taskType;
    private String internalMits;
    private String supportMits;
    private String status;
    private String remarks;
    private Long sprintId;
    private Long teamId;
    private LocalDate estimatedStartDate;
    private LocalDate estimatedEndDate;
    private LocalDate developmentStartDate;
    private LocalDate developmentEndDate;
    private LocalDate deliveryDate;
    private LocalDate liveDate;
    private LocalDate acceptanceDate;
    private String observerNames;
    private Integer storyPoints;
}
