package com.mislbd.report_manager.sprint_manager.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity(name="SPRINT_CONFIGURATION")
public class SprintConfigurationEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String configurationName;
    private String description;
    private String value;
    private Long teamId;
}
