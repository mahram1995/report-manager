package com.mislbd.report_manager.entity.admin;

import com.mislbd.report_manager.configuration.aopConfig.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Setter
@Getter
@AllArgsConstructor
@Entity(name = "bank")
public class BankEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_id_generator")
    @SequenceGenerator(
            name = "bank_id_generator",
            allocationSize = 1,
            sequenceName = "bank_sequence")
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
    public BankEntity() {
    }
}

