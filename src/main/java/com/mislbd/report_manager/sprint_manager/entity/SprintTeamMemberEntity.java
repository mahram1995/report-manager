package com.mislbd.report_manager.sprint_manager.entity;
import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="SPRINT_TEAM_MEMBER")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SprintTeamMemberEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long teamId;
    private Long memberId;
    private String memberRole;
    private LocalDate joinedAt;
 @Builder.Default private LocalDateTime createdAt=LocalDateTime.now();
 private LocalDateTime updatedAt;
 @PreUpdate void beforeUpdate() { updatedAt=LocalDateTime.now(); }
}
