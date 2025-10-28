package com.mislbd.report_manager.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mislbd.report_manager.configuration.annotation.CommandAttribute;
import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import com.mislbd.report_manager.configuration.aopConfig.domain.HasIdentity;
import com.mislbd.report_manager.entity.admin.UserEntity;

@CommandAttribute( name = "USER_MODIFICATION_COMMAND",
        description = "User Modification Command",
        module = "Admin")
public class UserModificationCommand extends Command<UserEntity>  implements HasIdentity {
    @JsonCreator
    public UserModificationCommand(@JsonProperty("payload")  UserEntity payload){
        super(payload);
    }
    @Override
    public String getIdentity() {
        return getPayload().getUserName();
    }
}
