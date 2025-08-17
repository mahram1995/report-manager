package com.mislbd.report_manager.validator;

import com.mislbd.report_manager.configuration.annotation.ValidateOperation;
import com.mislbd.report_manager.configuration.aopConfig.domain.OperationValidator;
import com.mislbd.report_manager.entity.admin.UserEntity;
import com.mislbd.report_manager.service.admin.AuthService;
import org.springframework.stereotype.Component;

@ValidateOperation(operation = "MODIFICATION_USER")
@Component
public class UpdateUserValidator implements OperationValidator<UserEntity> {
    private final AuthService authService;

    public UpdateUserValidator(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean validate(UserEntity dto) {

        return true;
    }
}
