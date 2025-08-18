package com.mislbd.report_manager.domain.admin;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDomain {
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
    private String isLogin;
}
