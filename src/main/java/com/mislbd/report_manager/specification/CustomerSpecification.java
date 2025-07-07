package com.mislbd.report_manager.specification;

import com.mislbd.report_manager.criteria.CustomerSearchCriteria;
import com.mislbd.report_manager.entity.CustomerEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecification  {

    public static Specification<CustomerEntity> getCustomerSpecification(CustomerSearchCriteria criteria) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (criteria.getCusId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("cusId"), criteria.getCusId()));
            }
            if (criteria.getIsActive() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("isActive"), criteria.getIsActive()));
            }
            if (criteria.getBranchId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("branchId"), criteria.getBranchId()));
            }
            if (criteria.getCustomerType() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("customerType"), criteria.getCustomerType()));
            }
            if (criteria.getGender() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("gender"), criteria.getGender()));
            }
            if (criteria.getMobileNumber() != null) {
                predicate = cb.and(predicate, cb.like(root.get("mobileNumber"), "%" + criteria.getMobileNumber() + "%"));
            }
            if (criteria.getCountryId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("countryId"), criteria.getCountryId()));
            }
            if (criteria.getDistrictId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("districtId"), criteria.getDistrictId()));
            }
            if (criteria.getDivisionId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("divisionId"), criteria.getDivisionId()));
            }
            if (criteria.getPostCodeId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("postCodeId"), criteria.getPostCodeId()));
            }
            if (criteria.getUpazillaId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("upazillaId"), criteria.getUpazillaId()));
            }
            if (criteria.getIsNidReceived() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("isNidReceived"), criteria.getIsNidReceived()));
            }
            if (criteria.getIsSuspended() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("isSuspended"), criteria.getIsSuspended()));
            }
            if (criteria.getIsVerified() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("isVerified"), criteria.getIsVerified()));
            }
            if (criteria.getIsNegativeListedCustomer() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("isNegativeListedCustomer"), criteria.getIsNegativeListedCustomer()));
            }

            return predicate;
        };
    }
}