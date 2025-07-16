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

    private String userName;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
    private String employeeId;
    private String userStatus;
    private String activeReason;
    private String disableReason;
    private String lockReason;

    private Long departmentId;
    private Long groupId;
    private Long userBranchId;

    @Lob
    private byte[] userPhoto;

    // Getters and setters
}
