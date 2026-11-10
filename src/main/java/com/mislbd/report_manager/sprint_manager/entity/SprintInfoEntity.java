package com.mislbd.report_manager.sprint_manager.entity;
import java.time.LocalDate;

import com.mislbd.report_manager.configuration.aopConfig.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="SPRINT_PLAN_INFO")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SprintInfoEntity  {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
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
