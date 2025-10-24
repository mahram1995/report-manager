package com.mislbd.report_manager.command;

import com.mislbd.report_manager.configuration.annotation.CommandAttribute;
import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import com.mislbd.report_manager.entity.admin.UserEntity;

@CommandAttribute( name = "CREATE_NEW_USER_COMMAND",
        description = "Crate New User Command",
        module = "Admin")
public class CreateNewUserCommand extends Command<UserEntity> {
    public CreateNewUserCommand(UserEntity payload){
        super(payload);
    }
}
