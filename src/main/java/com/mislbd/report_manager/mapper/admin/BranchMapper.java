package com.mislbd.report_manager.mapper.admin;

import com.mislbd.report_manager.domain.admin.BranchDomain;
import com.mislbd.report_manager.entity.admin.BranchEntity;

public class BranchMapper {
    // Domain -> Entity
    public static BranchEntity domainToEntity(BranchDomain domain) {
        if (domain == null) {
            return null;
        }

        BranchEntity entity = new BranchEntity();

        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setAddress(domain.getAddress());
        entity.setBranchId(domain.getBranchId());
        entity.setBankId(domain.getBankId());
        entity.setRoutingNumber(domain.getRoutingNumber());
        entity.setActive(domain.isActive());
        entity.setHeadOffice(domain.isHeadOffice());
        entity.setOnline(domain.isOnline());
        entity.setStatus(domain.getStatus());
        entity.setEmail(domain.getEmail());
        entity.setMobileNumber(domain.getMobileNumber());
        entity.setPhoneNumber(domain.getPhoneNumber());
        entity.setAddressLine(domain.getAddressLine());
        entity.setAdCode(domain.getAdCode());
        entity.setSwiftCode(domain.getSwiftCode());
        entity.setUpazillaCode(domain.getUpazillaCode());
        entity.setDistrictCode(domain.getDistrictCode());
        entity.setDivisionCode(domain.getDivisionCode());
        entity.setCountryCode(domain.getCountryCode());
        entity.setPostCode(domain.getPostCode());
        entity.setHouseNo(domain.getHouseNo());
        entity.setRoadNo(domain.getRoadNo());
        entity.setVillageName(domain.getVillageName());

        return entity;
    }

    // Entity -> Domain
    public static BranchDomain EntityToDomain(BranchEntity entity) {
        if (entity == null) {
            return null;
        }

        BranchDomain domain = new BranchDomain();

        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setAddress(entity.getAddress());
        domain.setBranchId(entity.getBranchId());
        domain.setBankId(entity.getBankId());
        domain.setRoutingNumber(entity.getRoutingNumber());
        domain.setActive(entity.isActive());
        domain.setHeadOffice(entity.isHeadOffice());
        domain.setOnline(entity.isOnline());
        domain.setStatus(entity.getStatus());
        domain.setEmail(entity.getEmail());
        domain.setMobileNumber(entity.getMobileNumber());
        domain.setPhoneNumber(entity.getPhoneNumber());
        domain.setAddressLine(entity.getAddressLine());
        domain.setAdCode(entity.getAdCode());
        domain.setSwiftCode(entity.getSwiftCode());
        domain.setUpazillaCode(entity.getUpazillaCode());
        domain.setDistrictCode(entity.getDistrictCode());
        domain.setDivisionCode(entity.getDivisionCode());
        domain.setCountryCode(entity.getCountryCode());
        domain.setPostCode(entity.getPostCode());
        domain.setHouseNo(entity.getHouseNo());
        domain.setRoadNo(entity.getRoadNo());
        domain.setVillageName(entity.getVillageName());

        return domain;
    }
}
