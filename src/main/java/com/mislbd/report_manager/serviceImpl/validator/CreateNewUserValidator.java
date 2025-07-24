package com.mislbd.report_manager.serviceImpl.validator;

import com.mislbd.report_manager.configuration.aopConfig.OperationValidator;
import com.mislbd.report_manager.entity.admin.UserEntity;
import com.mislbd.report_manager.repository.admin.SecuUserRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateNewUserValidator implements OperationValidator {
    private  final SecuUserRepository userRepo;

    public CreateNewUserValidator(SecuUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public String operationName() {
        return "CREATE_NEW_USER";
    }

    @Override
    public boolean validate(Object request) {
        // cast to specific request type and validate
        if (request instanceof UserEntity data) {
            if (userRepo.existsByUserName(data.getUserName())) {
                throw new RuntimeException("Username already exists");
            }
            // other logic
            return true;
        }
        return false;
    }
}

