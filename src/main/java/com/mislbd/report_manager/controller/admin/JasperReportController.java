package com.mislbd.report_manager.controller.admin;

import com.mislbd.report_manager.domain.admin.JasperReportDomain;
import com.mislbd.report_manager.domain.admin.JasperReportRequestDomain;
import com.mislbd.report_manager.service.admin.JasperReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/admin/jasper-reports")
@RequiredArgsConstructor
public class JasperReportController {

    private final JasperReportService jasperReportService;

    @PostMapping
    public ResponseEntity<JasperReportDomain> save(
            @RequestParam("reportFileName") String reportFileName,
            @RequestParam("dataSource") String dataSource,
            @RequestParam("reportFile") MultipartFile reportFile)
            throws Exception {

        JasperReportDomain domain = new JasperReportDomain();

        domain.setReportFileName(reportFileName);
        domain.setDataSource(dataSource);
        domain.setReportContent(reportFile.getBytes());

        JasperReportDomain saved =
                jasperReportService.save(domain);

        // Don't return BLOB content in normal save response
        saved.setReportContent(null);

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JasperReportDomain> update(
            @PathVariable Long id,
            @RequestParam("reportFileName") String reportFileName,
            @RequestParam("file") MultipartFile file)
            throws Exception {

        JasperReportDomain domain = new JasperReportDomain();

        domain.setReportFileName(reportFileName);
        domain.setReportContent(file.getBytes());

        JasperReportDomain updated =
                jasperReportService.update(id, domain);

        updated.setReportContent(null);

        return ResponseEntity.ok(updated);
    }

    @GetMapping("/get-jasper-reports")
    public ResponseEntity<List<JasperReportDomain>> getJasperReports() {

        List<JasperReportDomain> reports =
                jasperReportService.getByJasperReposts();

        return ResponseEntity.ok(reports);
    }


    @PostMapping(value = "/run-report")
    public ResponseEntity<byte[]> runReport(
            @RequestBody JasperReportRequestDomain request) throws Exception {

        byte[] report = jasperReportService.runReport(request).getBody();

        String baseFileName = request.getJasperFileName()
                .replaceFirst("(?i)\\.jrxml$", "");

        String fileName;
        MediaType contentType;
        String disposition;

        if ("xls".equalsIgnoreCase(request.getReportFormat())) {

            fileName = baseFileName + ".xls";
            contentType = MediaType.parseMediaType("application/vnd.ms-excel");
            disposition = "attachment; filename=\"" + fileName + "\"";

        } else if ("xlsx".equalsIgnoreCase(request.getReportFormat())) {

            fileName = baseFileName + ".xlsx";

            contentType = MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            );

            disposition = "attachment; filename=\"" + fileName + "\"";
        }else if ("docsdocs".equalsIgnoreCase(request.getReportFormat())) {

            fileName = baseFileName + ".docx";

            contentType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

            disposition = "attachment; filename=\"" + fileName + "\"";
        } else if ("csv".equalsIgnoreCase(request.getReportFormat())) {

            fileName = baseFileName + ".csv";

            contentType = MediaType.parseMediaType("text/csv");

            disposition =
                    "attachment; filename=\"" + fileName + "\"";
        }else if ("html".equalsIgnoreCase(request.getReportFormat())) {

            fileName = baseFileName + ".html";

            contentType = MediaType.TEXT_HTML;

            disposition =
                    "attachment; filename=\"" + fileName + "\"";
        } else {

            fileName = baseFileName + ".pdf";
            contentType = MediaType.APPLICATION_PDF;
            disposition = "inline; filename=\"" + fileName + "\"";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                .header(
                        HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                        HttpHeaders.CONTENT_DISPOSITION
                )
                .contentType(contentType)
                .body(report);
    }

}