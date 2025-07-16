package com.mislbd.report_manager.entity.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "RPT_USER_LOGIN_INFO")
public class UserLoginInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private String loginTerminal;
    private String loginDeviseName;

    // Getters and setters
}
