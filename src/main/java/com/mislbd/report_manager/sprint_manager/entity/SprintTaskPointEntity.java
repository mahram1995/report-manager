package com.mislbd.report_manager.sprint_manager.entity;
import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="SPRINT_TASK_ASSIGN_POINT")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SprintTaskPointEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long memberId;
    private String memberName;
    private String role;
    private BigDecimal points;
    private Long taskId;
    private Long sprintId;
    private Long teamId;

}
