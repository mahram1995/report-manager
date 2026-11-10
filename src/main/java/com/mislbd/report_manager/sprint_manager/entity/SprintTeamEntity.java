package com.mislbd.report_manager.sprint_manager.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="SPRINT_TEAM_INFO")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SprintTeamEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String teamName;
    private String description;
    private Boolean active;
 @Builder.Default private LocalDateTime createdAt=LocalDateTime.now();
 private LocalDateTime updatedAt;
 @PreUpdate void beforeUpdate() { updatedAt=LocalDateTime.now(); }
}
