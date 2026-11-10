package com.mislbd.report_manager.sprint_manager.entity;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="SPRINT_TASK_NOTES_HISTORY")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SprintTaskNoteHistoryEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private Long noteHistId;
    private String description;
    private String noteTitle;
    private LocalDateTime noteDate;
    private Long taskId;
    private String noteType;
    private Long sprintId;
    private Long teamId;
}
