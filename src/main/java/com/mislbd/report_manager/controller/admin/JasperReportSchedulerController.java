package com.mislbd.report_manager.controller.admin;

import com.mislbd.report_manager.domain.admin.JasperReportSchedulerDomain;
import com.mislbd.report_manager.service.admin.JasperReportSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/jasper-report-scheduler")
@RequiredArgsConstructor
public class JasperReportSchedulerController {

    private final JasperReportSchedulerService service;

    @PostMapping("/save")
    public JasperReportSchedulerDomain save(
            @RequestBody JasperReportSchedulerDomain entity
    ) {
        return service.save(entity);
    }

    @PutMapping("/update/{id}")
    public JasperReportSchedulerDomain update(
            @PathVariable Long id,
            @RequestBody JasperReportSchedulerDomain entity
    ) {
        return service.update(id, entity);
    }

    @GetMapping("/get-by-id/{id}")
    public JasperReportSchedulerDomain getById(
            @PathVariable Long id
    ) {
        return service.getById(id);
    }

    @PostMapping("/check-report-schedule")
    public ResponseEntity<String> checkReportSchedule(
            @RequestParam("scheduleId") Long scheduleId
    ) {
        return service.checkReportSchedule(scheduleId);
    }

    @GetMapping("/get-all")
    public List<JasperReportSchedulerDomain> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/delete/{id}")
    public void delete(
            @PathVariable Long id
    ) {
        service.delete(id);
    }

    @PutMapping("/activate/{id}")
    public JasperReportSchedulerDomain activate(
            @PathVariable Long id
    ) {
        return service.activate(id);
    }

    @PutMapping("/deactivate/{id}")
    public JasperReportSchedulerDomain deactivate(
            @PathVariable Long id
    ) {
        return service.deactivate(id);
    }
}