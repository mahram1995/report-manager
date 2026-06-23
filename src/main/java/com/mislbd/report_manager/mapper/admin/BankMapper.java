package com.mislbd.report_manager.mapper.admin;

import com.mislbd.report_manager.domain.admin.BankDomain;
import com.mislbd.report_manager.entity.admin.BankEntity;
import org.springframework.stereotype.Component;

@Component
public class BankMapper {
    private BankMapper() {
    }

    public static BankDomain entityToDto(BankEntity entity) {

        if (entity == null) {
            return null;
        }

        BankDomain dto = new BankDomain();

        dto.setId(entity.getId());
        dto.setIsActive(entity.getIsActive());
        dto.setStatus(entity.getStatus());
        dto.setBankCode(entity.getBankCode());
        dto.setOwnBank(entity.isOwnBank());
        dto.setBankName(entity.getBankName());
        dto.setSwiftCode(entity.getSwiftCode());
        dto.setBankSortName(entity.getBankSortName());
        dto.setOwnerType(entity.getOwnerType());
        dto.setWebsite(entity.getWebsite());
        dto.setFinancialInstitutionType(entity.getFinancialInstitutionType());
        dto.setCentralBankCode(entity.getCentralBankCode());
        dto.setAddressLine1(entity.getAddressLine1());
        dto.setAddressLine2(entity.getAddressLine2());
        dto.setDistrictCode(entity.getDistrictCode());
        dto.setDivisionCode(entity.getDivisionCode());
        dto.setCountryCode(entity.getCountryCode());
        dto.setPostCode(entity.getPostCode());
        dto.setHouseNo(entity.getHouseNo());
        dto.setRoadNo(entity.getRoadNo());
        dto.setVillageName(entity.getVillageName());
        dto.setState(entity.getState());
        dto.setCityName(entity.getCityName());
        dto.setPostOffice(entity.getPostOffice());
        dto.setZipCode(entity.getZipCode());

        return dto;
    }

    public  BankEntity domainToEntity(BankDomain dto) {

        if (dto == null) {
            return null;
        }

        BankEntity entity = new BankEntity();

        entity.setId(dto.getId());
        entity.setIsActive(dto.getIsActive());
        entity.setStatus(dto.getStatus());
        entity.setBankCode(dto.getBankCode());
        entity.setOwnBank(dto.isOwnBank());
        entity.setBankName(dto.getBankName());
        entity.setSwiftCode(dto.getSwiftCode());
        entity.setBankSortName(dto.getBankSortName());
        entity.setOwnerType(dto.getOwnerType());
        entity.setWebsite(dto.getWebsite());
        entity.setFinancialInstitutionType(dto.getFinancialInstitutionType());
        entity.setCentralBankCode(dto.getCentralBankCode());
        entity.setAddressLine1(dto.getAddressLine1());
        entity.setAddressLine2(dto.getAddressLine2());
        entity.setDistrictCode(dto.getDistrictCode());
        entity.setDivisionCode(dto.getDivisionCode());
        entity.setCountryCode(dto.getCountryCode());
        entity.setPostCode(dto.getPostCode());
        entity.setHouseNo(dto.getHouseNo());
        entity.setRoadNo(dto.getRoadNo());
        entity.setVillageName(dto.getVillageName());
        entity.setState(dto.getState());
        entity.setCityName(dto.getCityName());
        entity.setPostOffice(dto.getPostOffice());
        entity.setZipCode(dto.getZipCode());

        return entity;
    }
}
