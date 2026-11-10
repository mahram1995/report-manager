package com.mislbd.report_manager.sprint_manager.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="SPRINT_MEMBER_INFO")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SprintMemberEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String memberName;
    private String email;
    private String mobile;
    private String designation;
    private String passwordHash;
    private Boolean active;
 @Builder.Default private LocalDateTime createdAt=LocalDateTime.now();
 private LocalDateTime updatedAt;
 @PreUpdate void beforeUpdate() { updatedAt=LocalDateTime.now(); }
}
