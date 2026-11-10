package com.mislbd.report_manager.sprint_manager.entity;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="SPRINT_TASK_ASSIGN_MEMBER")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SprintTaskAssigneeEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long memberId;
    private Long taskId;
    private Long sprintId;
    private Long teamId;
    private String memberName;

}
