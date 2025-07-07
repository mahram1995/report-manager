package com.mislbd.report_manager.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "RPT_CUSTOMER")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "cis_id_generator")
    @SequenceGenerator(
            name = "cis_id_generator",
            allocationSize = 1,
            sequenceName = "cis_sequence")
    private Integer cusId;

    private String isActive;

    private Integer branchId;

    private String customerType;

    private String cusName;

    private String relationshipOfficer;

    private String isDeceasedCustomer;

    private String isPriorityCustomer;

    private Integer businessUnitId;

    private String secondaryRelationshipOfficer;

    private Integer customerClassId;

    private String cusShortName;

    private String addressLine;

    private String addressLineTwo;

    private String email;

    private String gender;

    private String mobileNumber;

    private Integer countryId;

    private Integer districtId;

    private Integer divisionId;

    private Integer postCodeId;

    private Integer upazillaId;

    private String title;

    private String isNidReceived;

    private String otherSystemCusId;

    private String isSuspended;

    private String foreignCity;

    private String foreignPostOffice;

    private String foreignState;

    private String foreignZipCode;

    private String isVerified;

    private String blackListReason;

    private String negativeListReason;

    private String isNegativeListedCustomer;
}


