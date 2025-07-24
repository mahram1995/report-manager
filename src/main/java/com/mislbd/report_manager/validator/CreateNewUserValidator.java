package com.mislbd.report_manager.validator;

import com.mislbd.report_manager.configuration.annotation.ValidateOperation;

import com.mislbd.report_manager.configuration.aopConfig.domain.OperationValidator;
import com.mislbd.report_manager.entity.admin.UserEntity;
import com.mislbd.report_manager.repository.admin.SecuUserRepository;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@ValidateOperation(operation = "CREATE_NEW_USER")
@Component
public class CreateNewUserValidator implements OperationValidator {
    private  final SecuUserRepository userRepo;

    public CreateNewUserValidator(SecuUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public boolean validate(Object domain) {
        // cast to specific request type and validate
        if (domain instanceof UserEntity data) {
            if (userRepo.existsByUserName(data.getUserName())) {
                throw new RuntimeException("Username already exists");
            }
            // other logic
            return true;
        }
        return false;
    }
}

