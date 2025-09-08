package com.mislbd.report_manager.controller.admin;

import com.mislbd.report_manager.configuration.aopConfig.entity.CommandEntity;
import com.mislbd.report_manager.entity.admin.UdfProfileEntity;
import com.mislbd.report_manager.service.admin.UdfProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/udf")
public class UdfProfileController {

    private final UdfProfileService service;

    public UdfProfileController(UdfProfileService service) {
        this.service = service;
    }

    @PostMapping("save-udf")
    public ResponseEntity<?> saveUdf(@RequestBody UdfProfileEntity profile) {

        return service.save(profile);
    }

    @PutMapping("update-udf")
    public ResponseEntity<?>  updateUdf(@RequestBody UdfProfileEntity profile) {
        return service.update(profile);
    }

    @PutMapping("updateById/{id}")
    public UdfProfileEntity updateById(@PathVariable Long id, @RequestBody UdfProfileEntity profile) {
        return service.updateById(id, profile);
    }

    @DeleteMapping("deleteById/{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }

    // Get all udf
    @GetMapping("/get-udfs")
    public Object getCommands(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "module", required = false) String module,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "asPage", defaultValue = "true") boolean asPage
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UdfProfileEntity> pageResult = service.getAll(name, module, pageable);

        if (asPage) {
            // return full Page object (with metadata: totalElements, totalPages, etc.)
            return pageResult;
        } else {
            // return only the list of entities
            return pageResult.getContent();
        }
    }

    @GetMapping("getUdfById")
    public UdfProfileEntity getById(
            @RequestParam(name = "id", required = false) Long id)
            {
        return service.getById(id);
    }
}
