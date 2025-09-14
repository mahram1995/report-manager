package com.mislbd.report_manager.repository.admin;

import com.mislbd.report_manager.entity.admin.UserReportGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportGroupRepository extends JpaRepository<UserReportGroupEntity, Long> {
}
