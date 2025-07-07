package com.mislbd.report_manager.specification;

import com.mislbd.report_manager.entity.CustomerEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecification  { public  static Specification<CustomerEntity> searchCustomer(
        long cusId,
        String isActive,
        Long branchId,
        String customerType,
        String gender,
        String mobileNumber,

        Long countryId,
        Long districtId,
        Long divisionId,
        Long postCodeId,
        Long upazillaId,

        String isNidReceived,
        String isSuspended,
        String isVerified,
        String isNegativeListedCustomer
){
    return (root, query, cb) -> {
        Predicate predicate = cb.conjunction();
       // query.orderBy(cb.desc(root.get("cusId")));

        if (isActive != null) {
            predicate = cb.and(predicate, cb.equal(root.get("isActive"), isActive));
        }
        if (String.valueOf(cusId) != null) {
            predicate = cb.and(predicate, cb.equal(root.get("cusId"), cusId));
        }
        if (branchId != null) {
            predicate = cb.and(predicate, cb.equal(root.get("branchId"), branchId));
        }
        if (customerType != null) {
            predicate = cb.and(predicate, cb.equal(root.get("customerType"), customerType));
        }
        if (gender != null) {
            predicate = cb.and(predicate, cb.equal(root.get("gender"), gender));
        }
        if (mobileNumber != null) {
            predicate = cb.and(predicate, cb.equal(root.get("mobileNumber"), mobileNumber));
        }
        if (countryId != null) {
            predicate = cb.and(predicate, cb.equal(root.get("countryId"), countryId));
        }
        if (districtId != null) {
            predicate = cb.and(predicate, cb.equal(root.get("districtId"), districtId));
        }
        if (divisionId != null) {
            predicate = cb.and(predicate, cb.equal(root.get("divisionId"), divisionId));
        }
        if (postCodeId != null) {
            predicate = cb.and(predicate, cb.equal(root.get("postCodeId"), postCodeId));
        }
        if (upazillaId != null) {
            predicate = cb.and(predicate, cb.equal(root.get("upazillaId"), upazillaId));
        }
        if (isNidReceived != null) {
            predicate = cb.and(predicate, cb.equal(root.get("isNidReceived"), isNidReceived));
        }
        if (isSuspended != null) {
            predicate = cb.and(predicate, cb.equal(root.get("isSuspended"), isSuspended));
        }
        if (isVerified != null) {
            predicate = cb.and(predicate, cb.equal(root.get("isVerified"), isVerified));
        }
        if (isNegativeListedCustomer != null) {
            predicate = cb.and(predicate, cb.equal(root.get("isNegativeListedCustomer"), isNegativeListedCustomer));
        }

        return predicate;
    };
}
}