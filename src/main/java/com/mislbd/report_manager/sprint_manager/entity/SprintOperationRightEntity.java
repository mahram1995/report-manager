package com.mislbd.report_manager.sprint_manager.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="SPRINT_OPERATION_RIGHT")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SprintOperationRightEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String rightName;
    private String description;

}
