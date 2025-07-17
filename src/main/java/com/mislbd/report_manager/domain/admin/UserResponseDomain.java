package com.mislbd.report_manager.domain.admin;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponseDomain {
    private String userName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Long departmentId;
    private Long groupId;
    private Long userBranchId;
    private String token;
}
