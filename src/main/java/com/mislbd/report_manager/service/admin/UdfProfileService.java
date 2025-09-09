package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.entity.admin.UdfProfileEntity;
import com.mislbd.report_manager.entity.admin.UdfUdfsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UdfProfileService {
    ResponseEntity<?> save(UdfProfileEntity profile);
    ResponseEntity<?> saveUserDefinedFiled(UdfUdfsEntity profile);
    ResponseEntity<?> updateUserDefinedFiled(UdfUdfsEntity profile);

    ResponseEntity<?>  update(UdfProfileEntity profile);
    UdfProfileEntity updateById(Long id, UdfProfileEntity profile);
    void deleteById(Long id);
    Page<UdfProfileEntity> getAll(String name, String module, Pageable pageable);
    UdfProfileEntity getById(Long id);

}

