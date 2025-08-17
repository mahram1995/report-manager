package com.mislbd.report_manager.entity.admin;

import com.mislbd.report_manager.configuration.aopConfig.entity.BaseEntity;
import com.mislbd.report_manager.configuration.aopConfig.listener.AuditEntityListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "RPT_SECU_USER")
@EntityListeners(AuditEntityListener.class)
public class UserEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_generator")
    @SequenceGenerator(name = "user_seq_generator", sequenceName = "ABABIL_USER_SEQ", allocationSize = 1)
    @Id
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
