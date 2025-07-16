package com.mislbd.report_manager.domain.admin;

import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Setter
@Getter
@ParameterObject
public class ChangePasswordDomain {
    String userName;
    String newPassword;
    String oldPassword;
}
