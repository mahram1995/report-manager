package com.mislbd.report_manager.repository.admin;

import com.mislbd.report_manager.entity.admin.DatabaseConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseConfigRepository extends JpaRepository<DatabaseConfigEntity, Long> {
}
