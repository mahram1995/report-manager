package com.mislbd.report_manager.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mislbd.report_manager.configuration.annotation.CommandAttribute;
import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import com.mislbd.report_manager.configuration.aopConfig.domain.HasIdentity;
import com.mislbd.report_manager.entity.admin.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@CommandAttribute( name = "CREATE_NEW_USER_COMMAND",
        description = "Crate New User Command",
        module = "Admin")
public class CreateNewUserCommand extends Command<UserEntity> implements HasIdentity {
    @JsonCreator
    public CreateNewUserCommand(@JsonProperty("payload") UserEntity payload){
        super(payload);
    }

    @Override
    public String getIdentity() {
        return getPayload().getUserName();
    }
}
