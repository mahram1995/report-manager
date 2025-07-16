package com.mislbd.report_manager.domain.admin;

import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.ModelAttribute;

@Setter
@Getter
@ParameterObject
public class AuthRequestDomain {
    String userName;
    String password;
    String loginTerminal;
}
