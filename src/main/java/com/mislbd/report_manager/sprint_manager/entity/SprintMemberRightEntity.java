package com.mislbd.report_manager.sprint_manager.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="SPRINT_ASSIGN_RIGHT")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SprintMemberRightEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long memberId;
    private Long rightId;
    private Boolean allowed;
 @Builder.Default private LocalDateTime createdAt=LocalDateTime.now();
 private LocalDateTime updatedAt;
 @PreUpdate void beforeUpdate() { updatedAt=LocalDateTime.now(); }
}
