package com.mislbd.report_manager.controller.admin;

import com.mislbd.report_manager.domain.admin.JasperReportDomain;
import com.mislbd.report_manager.domain.admin.JasperReportRequestDomain;
import com.mislbd.report_manager.projection.JasperReportHistAuditProjection;
import com.mislbd.report_manager.service.admin.JasperReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/jasper-reports")
@RequiredArgsConstructor
public class JasperReportController {

    private final JasperReportService jasperReportService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<JasperReportDomain>> save(
            @RequestParam("dataSource") String dataSource,
            @RequestParam("files") MultipartFile[] files) throws Exception {

        List<JasperReportDomain> savedReports = new ArrayList<>();

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue;
            }

            String fileName = file.getOriginalFilename();
            String extension = "";

            if (fileName != null && fileName.contains(".")) {
                extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            }

            if ("jrxml".equalsIgnoreCase(extension)
                    || "jasper".equalsIgnoreCase(extension)) {

                JasperReportDomain domain = new JasperReportDomain();

                domain.setReportFileName(fileName);
                domain.setDataSource(dataSource);
                domain.setReportContent(file.getBytes());

                JasperReportDomain saved = jasperReportService.save(domain);

                // Don't return BLOB content
                saved.setReportContent(null);

                savedReports.add(saved);
            }
            else if ("png".equalsIgnoreCase(extension)) {

                // Save PNG separately using your image service
//                jasperReportImageService.saveImage(
//                        fileName,
//                        dataSource,
//                        file.getBytes()
//                );
            }
            else {
                throw new IllegalArgumentException(
                        "Unsupported file format: " + fileName
                );
            }
        }

        return ResponseEntity.ok(savedReports);
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
                jasperReportService.getJasperReports();

        return ResponseEntity.ok(reports);
    }

    @GetMapping("/get-report-content")
    public  ResponseEntity<byte[]> getJasperReportContent(
            @RequestParam(name="reportId", required = false) Long reportId
    ) {
        return jasperReportService.getJasperReportContent(reportId);
    }

    @GetMapping("/get-report-hist")
    public ResponseEntity<List<JasperReportHistAuditProjection>> getAuditList(
            @RequestParam(name="reportFileName", required = true) String reportFileName){

        return ResponseEntity.ok(jasperReportService.getAuditList(reportFileName));
    }

    @GetMapping("/get-report-hist-content")
    public   ResponseEntity<byte[]> getJasperReportHistContent(
            @RequestParam(name="reportHistId", required = true) Long reportHistId)
    {
        return jasperReportService.getJasperReportHistContent(reportHistId);
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
                 .header(
                HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.attachment()
                        .filename(fileName, StandardCharsets.UTF_8)
                        .build()
                        .toString()
                ).contentType(contentType)
                .body(report);
    }

}