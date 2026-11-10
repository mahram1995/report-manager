package com.mislbd.report_manager.sprint_manager.entity;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="SPRINT_TASK_NOTES")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SprintTaskNoteEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long memberId;
    private String description;
    private String noteTitle;
    private LocalDateTime noteDate;
    private Long taskId;
    private String noteType;
    private Long sprintId;
    private Long teamId;

}
