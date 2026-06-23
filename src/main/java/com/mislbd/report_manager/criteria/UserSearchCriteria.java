package com.mislbd.report_manager.criteria;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserSearchCriteria {
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
    private boolean asPage;
}
