package com.mislbd.report_manager.repository.admin;

import com.mislbd.report_manager.entity.CustomerEntity;
import com.mislbd.report_manager.entity.admin.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SecuUserRepository extends JpaRepository<UserEntity, Long> , JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findByUserName(String userName);

    boolean existsByUserName(String userName);
}
