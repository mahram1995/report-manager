package com.mislbd.report_manager.criteria;

import lombok.Data;


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

    public String getIsActive() {
        return isActive;
    }

    public Integer getCusId() {
        return cusId;
    }

    public void setCusId(Integer cusId) {
        this.cusId = cusId;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Long divisionId) {
        this.divisionId = divisionId;
    }

    public Long getPostCodeId() {
        return postCodeId;
    }

    public void setPostCodeId(Long postCodeId) {
        this.postCodeId = postCodeId;
    }

    public Long getUpazillaId() {
        return upazillaId;
    }

    public void setUpazillaId(Long upazillaId) {
        this.upazillaId = upazillaId;
    }

    public String getIsNidReceived() {
        return isNidReceived;
    }

    public void setIsNidReceived(String isNidReceived) {
        this.isNidReceived = isNidReceived;
    }

    public String getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(String isSuspended) {
        this.isSuspended = isSuspended;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getIsNegativeListedCustomer() {
        return isNegativeListedCustomer;
    }

    public void setIsNegativeListedCustomer(String isNegativeListedCustomer) {
        this.isNegativeListedCustomer = isNegativeListedCustomer;
    }
}
