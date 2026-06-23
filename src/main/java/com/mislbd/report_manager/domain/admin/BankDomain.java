package com.mislbd.report_manager.domain.admin;

import lombok.Data;

@Data
public class BankDomain {
    private long id;
    private String isActive;
    private String status;
    private String bankCode;
    private boolean ownBank;
    private String bankName;
    private String swiftCode;
    private String bankSortName;
    private String ownerType;
    private String website;
    private String financialInstitutionType;
    private String centralBankCode;
    private String addressLine1;
    private String addressLine2;
    private String districtCode;
    private String divisionCode;
    private String countryCode;
    private String postCode;
    private String houseNo;
    private String roadNo;
    private String villageName;
    private String state ;
    private String cityName;
    private String postOffice;
    private String zipCode;
}
