package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.configuration.aopConfig.entity.ApiResponse;
import com.mislbd.report_manager.entity.admin.UdfProfileEntity;
import com.mislbd.report_manager.repository.admin.UdfProfileRepository;
import com.mislbd.report_manager.service.admin.UdfProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UdfProfileServiceImpl implements UdfProfileService {

    private final UdfProfileRepository repository;

    public UdfProfileServiceImpl(UdfProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<?> save(UdfProfileEntity profile) {
        if(repository.existsByCode(profile.getCode())){
         throw  new RuntimeException("Code already exists");

        };
        if(repository.existsByName(profile.getName())){
            throw new RuntimeException("Name already exists");

        }

        repository.save(profile);
        return ResponseEntity.ok(new ApiResponse<>("UDF create successfully", true,null));
    }

    @Override
    public UdfProfileEntity update(UdfProfileEntity profile) {
        return repository.save(profile);
    }

    @Override
    public UdfProfileEntity updateById(Long id, UdfProfileEntity profile) {
        UdfProfileEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        profile.setId(existing.getId());
        return repository.save(profile);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<UdfProfileEntity> getAll(String name, String module, Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public UdfProfileEntity getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
    }
}

