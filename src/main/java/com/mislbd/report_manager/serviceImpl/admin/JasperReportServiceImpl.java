package com.mislbd.report_manager.serviceImpl.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mislbd.report_manager.configuration.aopConfig.service.CommonService;
import com.mislbd.report_manager.domain.admin.JasperReportDomain;
import com.mislbd.report_manager.domain.admin.JasperReportRequestDomain;
import com.mislbd.report_manager.entity.admin.DatabaseConfigEntity;
import com.mislbd.report_manager.entity.admin.JasperReportEntity;
import com.mislbd.report_manager.entity.admin.JasperReportAuditLogEntity;
import com.mislbd.report_manager.entity.admin.JasperReportHistEntity;
import com.mislbd.report_manager.exception.ReportExecutionException;
import com.mislbd.report_manager.mapper.admin.JasperReportMapper;
import com.mislbd.report_manager.projection.JasperReportHistAuditProjection;
import com.mislbd.report_manager.repository.admin.DatabaseConfigRepository;
import com.mislbd.report_manager.repository.admin.JasperReportHistRepository;
import com.mislbd.report_manager.repository.admin.JasperReportRepository;
import com.mislbd.report_manager.service.admin.JasperReportAuditLogService;
import com.mislbd.report_manager.service.admin.JasperReportService;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialClob;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class JasperReportServiceImpl implements JasperReportService {

    private final JasperReportRepository jasperReportRepository;
    private final JasperReportAuditLogService jasperReportErrorLogService;
    private final JasperReportHistRepository histRepository;
    private final JasperReportMapper jasperReportMapper;
    private final CommonService commonService;
    private final DatabaseConfigRepository databaseConfigRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public JasperReportDomain save(JasperReportDomain domain) {

        if (domain == null) {
            throw new IllegalArgumentException("Report data cannot be null");
        }

        if (domain.getReportFileName() == null
                || domain.getReportFileName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Report file name is required");
        }

        if (domain.getReportContent() == null
                || domain.getReportContent().length == 0) {
            throw new IllegalArgumentException(
                    "Report content is required");
        }

        if (jasperReportRepository.existsByReportFileName(domain.getReportFileName())) {

            Optional<JasperReportEntity> entity = jasperReportRepository.findByReportFileName(domain.getReportFileName());
            JasperReportHistEntity hisEntity = jasperReportMapper.EntityToHistEntity(entity);

            histRepository.save(hisEntity);
            return update(entity.get().getId(), domain);
        }

        JasperReportEntity entity = jasperReportMapper.domainToEntity(domain);

        JasperReportEntity savedEntity = jasperReportRepository.save(entity);

        return jasperReportMapper.entityToDomain(savedEntity);
    }


    @Override
    public List<JasperReportHistAuditProjection> getAuditList(String reportFileName) {
        return histRepository.findAuditList(reportFileName);
    }

    @Override
    @Transactional
    public JasperReportDomain update(
            Long id,
            JasperReportDomain domain) {

        if (id == null) {
            throw new IllegalArgumentException("Report ID is required");
        }

        JasperReportEntity entity =
                jasperReportRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Report not found with id: " + id));

        if (domain.getReportFileName() == null
                || domain.getReportFileName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Report file name is required");
        }

        if(id.toString()!=null){
            // Update audit information
            entity.setUpdateBy(commonService.getCurrentUsername());
            entity.setUpdateDateTime(LocalDateTime.now());
            entity.setUpdateTerminal(commonService.getTerminal());
            entity.setUpdateAgent(commonService.getUserAgent());
        }

        if (domain.getReportContent() == null
                || domain.getReportContent().length == 0) {
            throw new IllegalArgumentException(
                    "Report content is required");
        }


        jasperReportMapper.updateEntity(domain, entity);

        JasperReportEntity updatedEntity = jasperReportRepository.save(entity);

        return jasperReportMapper.entityToDomain(updatedEntity);
    }

    @Override
    @Transactional()
    public JasperReportDomain getByReportFileName(
            String reportFileName) {

        if (reportFileName == null
                || reportFileName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Report file name is required");
        }

        JasperReportEntity entity =
                jasperReportRepository
                        .findByReportFileName(reportFileName)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Report not found: "
                                                + reportFileName));

        return jasperReportMapper.entityToDomain(entity);
    }

    @Override
    public List<JasperReportDomain> getJasperReports() {
        List<JasperReportEntity> entities = jasperReportRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        return entities.stream().map(jasperReportMapper::entityToDomain).collect(Collectors.toList());
    }

    private String getSafeFileName(String fileName) {

        if (fileName == null || fileName.trim().isEmpty()) {
            return "report";
        }

        // Remove directory paths, if any
        fileName = fileName.replace("\\", "/");

        return fileName.substring(fileName.lastIndexOf("/") + 1);
    }

    private String getUniqueFileName(String fileName, Set<String> usedFileNames) {

        String name = fileName;
        String extension = "";

        int dotIndex = fileName.lastIndexOf(".");

        if (dotIndex > 0) {
            name = fileName.substring(0, dotIndex);
            extension = fileName.substring(dotIndex);
        }

        String uniqueName = fileName;
        int count = 1;

        while (usedFileNames.contains(uniqueName.toLowerCase())) {
            uniqueName = name + "_" + count + extension;
            count++;
        }

        usedFileNames.add(uniqueName.toLowerCase());

        return uniqueName;
    }
    @Override
    public ResponseEntity<byte[]> getJasperReportContent(Long id) {

        try {
            // Download a specific report
            if (id != null) {

                JasperReportEntity entity = jasperReportRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException(
                                "Report not found for ID: " + id));

                byte[] reportContent = entity.getReportContent();

                if (reportContent == null || reportContent.length == 0) {
                    throw new RuntimeException("Report content is empty.");
                }

                String fileName = getSafeFileName(entity.getReportFileName());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDisposition(
                        ContentDisposition.attachment()
                                .filename(fileName)
                                .build()
                );

                return new ResponseEntity<>(reportContent, headers, HttpStatus.OK);
            }

            // Download all reports as a ZIP file
            List<JasperReportEntity> reports = jasperReportRepository.findAll();

            if (reports.isEmpty()) {
                throw new RuntimeException("No reports found.");
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            try (ZipOutputStream zipOutputStream =
                         new ZipOutputStream(byteArrayOutputStream)) {

                Set<String> usedFileNames = new HashSet<>();

                for (JasperReportEntity report : reports) {

                    byte[] reportContent = report.getReportContent();

                    if (reportContent == null || reportContent.length == 0) {
                        continue;
                    }

                    String fileName = getSafeFileName(report.getReportFileName());

                    // Avoid duplicate filenames inside ZIP
                    String zipEntryName = getUniqueFileName(fileName, usedFileNames);

                    ZipEntry zipEntry = new ZipEntry(zipEntryName);
                    zipOutputStream.putNextEntry(zipEntry);

                    zipOutputStream.write(reportContent);

                    zipOutputStream.closeEntry();
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(
                    ContentDisposition.attachment()
                            .filename("reports.zip")
                            .build()
            );

            return new ResponseEntity<>(
                    byteArrayOutputStream.toByteArray(),
                    headers,
                    HttpStatus.OK
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to download report files.", e);
        }
    }

    @Override
    public ResponseEntity<byte[]> getJasperReportHistContent(Long histId) {
        try {
                JasperReportHistEntity entity = histRepository.findById(histId)
                        .orElseThrow(() -> new RuntimeException(
                                "Report not found for ID: " + histId));

                byte[] reportContent = entity.getReportContent();

                if (reportContent == null || reportContent.length == 0) {
                    throw new RuntimeException("Report content is empty.");
                }

                String fileName = getSafeFileName(entity.getReportFileName());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDisposition(
                        ContentDisposition.attachment()
                                .filename(fileName)
                                .build()
                );

                return new ResponseEntity<>(reportContent, headers, HttpStatus.OK);


        } catch (Exception e) {
            throw new RuntimeException("Failed to download report files.", e);
        }

    }

    public static Map<String, Object> convertDateParameters(
            Map<String, Object> parameters) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        parameters.replaceAll((key, value) -> {

            if (value instanceof String) {
                String dateValue = ((String) value).trim();

                if (dateValue.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    try {
                        java.util.Date parsedDate = sdf.parse(dateValue);

                        return new java.sql.Date(parsedDate.getTime());

                    } catch (ParseException e) {
                        throw new IllegalArgumentException(
                                "Invalid date parameter: " + key, e);
                    }
                }
            }

            return value;
        });

        return parameters;
    }

    @Override
    public ResponseEntity<byte[]> runReport(JasperReportRequestDomain request) throws IOException, JRException, SQLException {
        String reportFileName = request.getJasperFileName();
        Optional<JasperReportEntity> entity = jasperReportRepository.findByReportFileName(reportFileName);

        long auditLogId;

        if (entity.isEmpty()) {
            throw new RuntimeException("File not Found: " + reportFileName);
        }
        request.setParameters(convertDateParameters(request.getParameters()));

        if (entity.get().getReportContent() == null
                || entity.get().getReportContent().length == 0) {

            throw new RuntimeException(
                    "Jasper report content is empty: "
                            + reportFileName);
        }

        byte[] reportBytes = entity.get().getReportContent();


        // Get database connection
        DatabaseConfigEntity db = getDatabase(Long.valueOf(entity.get().getDataSource())).orElseThrow(() -> new RuntimeException("Database not found"));
        DataSource dataSource = createDataSource(db);
        // Get parameters
        Map<String, Object> reportParameters = request.getParameters() != null
                ? new HashMap<>(request.getParameters())
                : new HashMap<>();


        try (Connection connection = dataSource.getConnection()) {

            // entry the initial log
            auditLogId = saveAuditData(request, "PROCESSING", null, "",null);

            // String filePath = "C:\\Users\\User\\Downloads\\MITS_20028_CL_REPORT_OLD\\MITS_20028_CL_REPORT_OLD\\CL1_TOPSHEET_OLD.jasper";
            //File file = new File(filePath);
            //JasperReport jasperReport=JasperCompileManager.compileReport(filePath);
            // JasperReport jasperReport =(JasperReport) JRLoader.loadObject(file);


            JasperReport jasperReport = JasperCompileManager.compileReport(new ByteArrayInputStream(reportBytes));


            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportParameters, connection);

            if ("xls".equalsIgnoreCase(request.getReportFormat())) {
                JRXlsExporter exporter = new JRXlsExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

                exporter.exportReport();
                // save audit log
                saveAuditData(request, "SUCCESS", outputStream.toByteArray(), "",auditLogId);

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(outputStream.toByteArray());
            }

            if ("xlsx".equalsIgnoreCase(request.getReportFormat())) {

                JRXlsxExporter exporter = new JRXlsxExporter();

                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

                exporter.exportReport();
                // save audit log
                saveAuditData(request, "SUCCESS", outputStream.toByteArray(), "",auditLogId);

                return ResponseEntity.ok().contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )).body(outputStream.toByteArray());
            }

            if ("docs".equalsIgnoreCase(request.getReportFormat())) {

                JRDocxExporter exporter = new JRDocxExporter();

                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

                exporter.exportReport();
                // save audit log
                saveAuditData(request, "SUCCESS", outputStream.toByteArray(), "",auditLogId);

                return ResponseEntity.ok().contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                )).body(outputStream.toByteArray());
            }
            if ("csv".equalsIgnoreCase(request.getReportFormat())) {

                JRCsvExporter exporter = new JRCsvExporter();

                exporter.setExporterInput(
                        new SimpleExporterInput(jasperPrint)
                );

                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream();

                exporter.setExporterOutput(
                        new SimpleWriterExporterOutput(
                                outputStream,
                                String.valueOf(StandardCharsets.UTF_8)
                        )
                );

                exporter.exportReport();
                // save audit log
                saveAuditData(request, "SUCCESS", outputStream.toByteArray(), "",auditLogId);

                return ResponseEntity.ok()
                        .contentType(
                                MediaType.parseMediaType("text/csv")
                        )
                        .body(outputStream.toByteArray());
            }
            if ("html".equalsIgnoreCase(request.getReportFormat())) {

                HtmlExporter exporter = new HtmlExporter();

                exporter.setExporterInput(
                        new SimpleExporterInput(jasperPrint)
                );

                ByteArrayOutputStream outputStream =
                        new ByteArrayOutputStream();

                exporter.setExporterOutput(
                        new SimpleHtmlExporterOutput(outputStream)
                );

                exporter.exportReport();


                // save audit log
                saveAuditData(request, "SUCCESS", outputStream.toByteArray(), "",auditLogId);

                return ResponseEntity.ok()
                        .contentType(
                                MediaType.TEXT_HTML
                        )
                        .body(outputStream.toByteArray());
            }

            byte[] reportData = JasperExportManager.exportReportToPdf(jasperPrint);

            // save audit log
            saveAuditData(request, "SUCCESS", reportData, "", auditLogId);

            return ResponseEntity.ok(reportData);

        } catch (JRException e) {

            String errorMessage = getFullErrorMessage(e);
            String errorShortMessage = getRootErrorMessage(e);

            // Save audit log
            Long errorId = saveAuditData(
                    request,
                    "NOT-SUCCESS",
                    null,
                    errorMessage,
                    null
            );

            throw new ReportExecutionException(
                    errorShortMessage + " Error Log id: " + errorId
            );
        }
    }

    public static String getRootErrorMessage(Throwable exception) {

        if (exception == null) {
            return "Unknown error occurred";
        }

        Throwable rootCause = exception;

        // Find the deepest cause safely
        while (rootCause.getCause() != null
                && rootCause.getCause() != rootCause) {

            rootCause = rootCause.getCause();
        }

        String message = rootCause.getMessage();

        if (message == null || message.trim().isEmpty()) {
            message = rootCause.toString();
        }

        return message;
    }

    public static String getFullErrorMessage(Throwable exception) {

        if (exception == null) {
            return "Unknown error occurred";
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        exception.printStackTrace(pw);

        return sw.toString();
    }

    public Optional<DatabaseConfigEntity> getDatabase(Long id) {
        return databaseConfigRepository.findById(id);


    }

    private DataSource createDataSource(DatabaseConfigEntity db) {

        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl(db.getUrl());
        dataSource.setUsername(db.getUsername());
        dataSource.setPassword(db.getPassword());
        dataSource.setDriverClassName(db.getDriverClass());

        // For report execution
        dataSource.setMaximumPoolSize(5);
        dataSource.setMinimumIdle(1);

        return dataSource;
    }

    ;


    private Long saveAuditData(
            JasperReportRequestDomain request,
            String status,
            byte[] reportData,
            String errorMage,
            Long auditLogId
    ) throws SQLException {

        JasperReportAuditLogEntity entity;

        // Get existing audit record or create a new one
        if (auditLogId != null) {
            entity = jasperReportErrorLogService.getAuditLogById(auditLogId);
        } else {
            entity = null;
        }

        boolean isNew = (entity == null);

        if (isNew) {
            entity = new JasperReportAuditLogEntity();
            entity.setStartTime(new Date());
        }

        Date endTime = new Date();

        // Calculate duration for existing audit record
        if (!isNew && entity.getStartTime() != null) {

            long durationMillis =
                    endTime.getTime() - entity.getStartTime().getTime();


            entity.setEndTime(endTime);
            entity.setDurationInms(durationMillis);
        }

        // Convert error message to CLOB
        Clob clob = null;

        if (errorMage != null && !errorMage.isEmpty()) {
            clob = new SerialClob(errorMage.toCharArray());
        }

        entity.setError(clob);
        entity.setReportFileName(request.getJasperFileName());
        entity.setReportName(request.getReportName());
        entity.setEntryUser(commonService.getCurrentUsername());
        entity.setTerminal(commonService.getTerminal());
        entity.setUserClient(commonService.getUserAgent());
        entity.setStatus(status);
        entity.setReportData(reportData);
        entity.setEntryDate(new Date());
        entity.setReportFormat(request.getReportFormat());
        entity.setParameter(convertMapToClob(request.getParameters()));

        return jasperReportErrorLogService.saveErrorLog(entity);
    }


    private Clob convertMapToClob(Map<String, Object> parameters) {
        try {
            String json = objectMapper.writeValueAsString(parameters);

            return new SerialClob(json.toCharArray());

        } catch (Exception e) {
            throw new RuntimeException("Failed to convert parameters to JSON CLOB", e);
        }
    }


}