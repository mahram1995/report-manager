package com.mislbd.report_manager.mapper.admin;

import com.mislbd.report_manager.domain.admin.UserDomain;
import com.mislbd.report_manager.entity.admin.UserEntity;

import java.util.Objects;

public class UserMapper {

    // Convert Entity → Domain
    public static UserDomain entityToDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        UserDomain domain = new UserDomain();
        domain.setId(entity.getId());
        domain.setUserName(entity.getUserName());
        domain.setPassword(null);
        domain.setFirstName(entity.getFirstName());
        domain.setMiddleName(entity.getMiddleName());
        domain.setLastName(entity.getLastName());
        domain.setEmail(entity.getEmail());
        domain.setPhone(entity.getPhone());
        domain.setEmployeeId(entity.getEmployeeId());
        domain.setUserStatus(entity.getUserStatus());
        domain.setActiveReason(entity.getActiveReason());
        domain.setDisableReason(entity.getDisableReason());
        domain.setLockReason(entity.getLockReason());
        domain.setDepartmentId(entity.getDepartmentId());
        domain.setGroupId(entity.getGroupId());
        domain.setUserBranchId(entity.getUserBranchId());
        domain.setIsLogin(entity.getIsLogin());

        return domain;
    }

    // Convert Domain → Entity
    public static UserEntity domainToEntity(UserDomain domain) {
        if (domain == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setUserName(domain.getUserName());
        entity.setPassword(domain.getPassword());
        entity.setFirstName(domain.getFirstName());
        entity.setMiddleName(domain.getMiddleName());
        entity.setLastName(domain.getLastName());
        entity.setEmail(domain.getEmail());
        entity.setPhone(domain.getPhone());
        entity.setEmployeeId(domain.getEmployeeId());
        entity.setUserStatus(domain.getUserStatus());
        entity.setActiveReason(domain.getActiveReason());
        entity.setDisableReason(domain.getDisableReason());
        entity.setLockReason(domain.getLockReason());
        entity.setDepartmentId(domain.getDepartmentId());
        entity.setGroupId(domain.getGroupId());
        entity.setUserBranchId(domain.getUserBranchId());
        entity.setIsLogin(domain.getIsLogin());

        return entity;
    }
}
