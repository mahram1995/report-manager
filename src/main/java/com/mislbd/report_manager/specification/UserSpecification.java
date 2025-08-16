package com.mislbd.report_manager.specification;

import com.mislbd.report_manager.criteria.CustomerSearchCriteria;
import com.mislbd.report_manager.criteria.UserSearchCriteria;
import com.mislbd.report_manager.entity.CustomerEntity;
import com.mislbd.report_manager.entity.admin.UserEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<UserEntity> getUserSpecification(UserSearchCriteria criteria) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (criteria.getUserName() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("userName"), criteria.getUserName()));
            }
            if (criteria.getPassword() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("password"), criteria.getPassword()));
            }
            if (criteria.getFirstName() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("firstName"), criteria.getFirstName()));
            }
            if (criteria.getMiddleName() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("middleName"), criteria.getMiddleName()));
            }
            if (criteria.getLastName() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("lastName"), criteria.getLastName()));
            }
            if (criteria.getEmail() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("email"), criteria.getEmail()));
            }
            if (criteria.getPhone() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("phone"), criteria.getPhone()));
            }
            if (criteria.getEmployeeId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("employeeId"), criteria.getEmployeeId()));
            }
            if (criteria.getUserStatus() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("userStatus"), criteria.getUserStatus()));
            }
            if (criteria.getActiveReason() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("activeReason"), criteria.getActiveReason()));
            }
            if (criteria.getDisableReason() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("disableReason"), criteria.getDisableReason()));
            }
            if (criteria.getLockReason() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("lockReason"), criteria.getLockReason()));
            }
            if (criteria.getDepartmentId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("departmentId"), criteria.getDepartmentId()));
            }
            if (criteria.getGroupId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("groupId"), criteria.getGroupId()));
            }
            if (criteria.getUserBranchId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("userBranchId"), criteria.getUserBranchId()));
            }
            if (criteria.getIsLogin() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("isLogin"), criteria.getIsLogin()));
            }



            return predicate;
        };
    }
}
