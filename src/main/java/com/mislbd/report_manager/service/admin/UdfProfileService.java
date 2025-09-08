package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.entity.admin.UdfProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UdfProfileService {
    ResponseEntity<?> save(UdfProfileEntity profile);
    ResponseEntity<?>  update(UdfProfileEntity profile);
    UdfProfileEntity updateById(Long id, UdfProfileEntity profile);
    void deleteById(Long id);
    Page<UdfProfileEntity> getAll(String name, String module, Pageable pageable);
    UdfProfileEntity getById(Long id);

}

