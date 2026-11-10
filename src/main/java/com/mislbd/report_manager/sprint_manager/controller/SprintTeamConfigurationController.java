package com.mislbd.report_manager.sprint_manager.controller;

import com.mislbd.report_manager.sprint_manager.entity.SprintTeamConfigurationEntity;
import com.mislbd.report_manager.sprint_manager.service.SprintTeamConfigurationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sprint-team-configuration")
public class SprintTeamConfigurationController {
    private final SprintTeamConfigurationService service;

    public SprintTeamConfigurationController(SprintTeamConfigurationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SprintTeamConfigurationEntity> save(@RequestBody SprintTeamConfigurationEntity entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SprintTeamConfigurationEntity> update(@PathVariable Long id, @RequestBody SprintTeamConfigurationEntity entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SprintTeamConfigurationEntity> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "false") boolean paged,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        if (paged) {
            Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));
            Page<SprintTeamConfigurationEntity> result = service.getAll(pageable);
            return ResponseEntity.ok(result);
        }
        List<SprintTeamConfigurationEntity> result = service.getAll();
        return ResponseEntity.ok(result);
    }
}
