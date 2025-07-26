package com.mislbd.report_manager.validator;

import com.mislbd.report_manager.configuration.annotation.ValidateOperation;
import com.mislbd.report_manager.configuration.aopConfig.domain.OperationValidator;
import com.mislbd.report_manager.entity.admin.UserEntity;
import com.mislbd.report_manager.service.admin.AuthService;
import org.springframework.stereotype.Component;

@ValidateOperation(operation = "CREATE_NEW_USER")
@Component
public class CreateUserValidator implements OperationValidator<UserEntity> {
    private final AuthService authService;

    public CreateUserValidator(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean validate(UserEntity dto) {
        if(authService.existByUserName(dto.getUserName()))
            throw new RuntimeException("User Name already exist");
        if (dto.getUserName() == null || dto.getUserName().isBlank()) {
            throw new RuntimeException("Username is required.");
        }
        if (dto.getPhone() == null || dto.getPhone().isBlank()) {
            throw new RuntimeException("Phone number is required.");
        }
        return true;
    }
}
