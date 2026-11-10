package com.mislbd.report_manager.sprint_manager.entity;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="SPRINT_TASK_ASSIGHN_MEMBER_HISTORY")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SprintAssignmentHistoryEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long taskId;
    private Long memberId;
    private String assignmentType;
    private Long changedByMemberId;
    private LocalDateTime changedAt;
}
