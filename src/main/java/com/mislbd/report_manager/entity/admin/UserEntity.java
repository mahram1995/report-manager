package com.mislbd.report_manager.entity.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "RPT_SECU_USER")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userName;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;
    private String employeeId;
    private String userStatus;
    private String activeReason;
    private String disableReason;
    private String lockReason;

    private Long departmentId;
    private Long groupId;
    private Long userBranchId;
    private String isLogin;

    @Lob
    private byte[] userPhoto;

    // Getters and setters
}
