package com.mislbd.report_manager.sprint_manager.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="SPRINT_CONFIGURATION_MAPPING")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SprintTeamConfigurationEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long configurationId;
    private Long teamId;
    private String value;
 @Builder.Default private LocalDateTime createdAt=LocalDateTime.now();
 private LocalDateTime updatedAt;
 @PreUpdate void beforeUpdate() { updatedAt=LocalDateTime.now(); }
}
