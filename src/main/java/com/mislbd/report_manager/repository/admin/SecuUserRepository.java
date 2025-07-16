package com.mislbd.report_manager.repository.admin;

import com.mislbd.report_manager.entity.admin.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecuUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserName(String userName);
}
