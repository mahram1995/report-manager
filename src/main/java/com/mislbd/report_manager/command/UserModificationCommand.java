package com.mislbd.report_manager.command;

import com.mislbd.report_manager.configuration.annotation.CommandAttribute;
import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import com.mislbd.report_manager.entity.admin.UserEntity;

@CommandAttribute( name = "USER_MODIFICATION_COMMAND",
        description = "User Modification Command",
        module = "Admin")
public class UserModificationCommand extends Command<UserEntity> {
    public UserModificationCommand(UserEntity payload){
        super(payload);
    }
}
