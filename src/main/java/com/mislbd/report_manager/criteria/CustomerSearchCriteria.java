package com.mislbd.report_manager.criteria;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class CustomerSearchCriteria {
    Integer cusId;
    private String isActive;
    private Long branchId;
    private String customerType;
    private String gender;
    private String mobileNumber;

    private Long countryId;
    private Long districtId;
    private Long divisionId;
    private Long postCodeId;
    private Long upazillaId;

    private String isNidReceived;
    private String isSuspended;
    private String isVerified;
    private String isNegativeListedCustomer;

}
