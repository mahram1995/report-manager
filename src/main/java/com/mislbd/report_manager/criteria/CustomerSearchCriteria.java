package com.mislbd.report_manager.criteria;

import lombok.Data;

@Data
public class CustomerSearchCriteria {
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
    private String isNegativeListedCustome;
}
