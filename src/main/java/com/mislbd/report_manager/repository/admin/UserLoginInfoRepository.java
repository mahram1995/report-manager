package com.mislbd.report_manager.repository.admin;

import com.mislbd.report_manager.entity.admin.UserLoginInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserLoginInfoRepository extends JpaRepository<UserLoginInfoEntity, Long> {
    List<UserLoginInfoEntity> findByUserId(Long userId);

    Optional<Object> findTopByUserIdOrderByLoginTimeDesc(Long id);
}
