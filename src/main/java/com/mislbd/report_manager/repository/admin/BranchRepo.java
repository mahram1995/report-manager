package com.mislbd.report_manager.repository.admin;


import com.mislbd.report_manager.entity.admin.BranchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepo extends JpaRepository<BranchEntity, Integer> {

    @Query("""
           SELECT b
           FROM branch b
           WHERE LOWER(b.name) LIKE LOWER(:name)
             AND (:bankId IS NULL OR b.bankId = :bankId)
             AND (:branchId IS NULL OR b.branchId = :branchId)
           """)
    Page<BranchEntity> findBranches(
            @Param("name") String name,
            @Param("bankId") String bankId,
            @Param("branchId") String branchId,
            Pageable pageable);


    @Query("""
           SELECT b
           FROM branch b
           WHERE LOWER(b.name) LIKE LOWER(:name)
             AND (:bankId IS NULL OR b.bankId = :bankId)
             AND (:branchId IS NULL OR b.branchId = :branchId)
           """)
    List<BranchEntity> findBranches(
            @Param("name") String name,
            @Param("bankId") String bankId,
            @Param("branchId") String branchId);
}
