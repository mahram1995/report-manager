package com.mislbd.report_manager.repository.admin;

import com.mislbd.report_manager.entity.admin.UdfProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UdfProfileRepository extends JpaRepository<UdfProfileEntity, Long>  {
    boolean existsByCode(String code);

    boolean existsByName(String name);
}