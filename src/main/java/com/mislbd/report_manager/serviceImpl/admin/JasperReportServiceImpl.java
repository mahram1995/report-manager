package com.mislbd.report_manager.serviceImpl.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mislbd.jasper.CustomFunctions;
import com.mislbd.report_manager.configuration.aopConfig.service.CommonService;
import com.mislbd.report_manager.configuration.aopConfig.service.UtilityService;
import com.mislbd.report_manager.domain.admin.JasperReportDomain;
import com.mislbd.report_manager.domain.admin.JasperReportRequestDomain;
import com.mislbd.report_manager.entity.admin.DatabaseConfigEntity;
import com.mislbd.report_manager.entity.admin.JasperReportEntity;
import com.mislbd.report_manager.entity.admin.JasperReportAuditLogEntity;
import com.mislbd.report_manager.entity.admin.JasperReportHistEntity;
import com.mislbd.report_manager.exception.ExceptionUtils;
import com.mislbd.report_manager.exception.ReportExecutionException;
import com.mislbd.report_manager.mapper.admin.JasperReportMapper;
import com.mislbd.report_manager.projection.JasperReportHistAuditProjection;
import com.mislbd.report_manager.repository.admin.DatabaseConfigRepository;
import com.mislbd.report_manager.repository.admin.JasperReportHistRepository;
import com.mislbd.report_manager.repository.admin.JasperReportRepository;
import com.mislbd.report_manager.service.admin.DatabaseSubReportService;
import com.mislbd.report_manager.service.admin.JasperQrcodeGenerationService;
import com.mislbd.report_manager.service.admin.JasperReportAuditLogService;
import com.mislbd.report_manager.service.admin.JasperReportService;
import com.mislbd.report_manager.utility.AppConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.transaction.annotation.Transactional;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.fill.AsynchronousFillHandle;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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
import org.springframework.transaction.annotation.Propagation;

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
    private final DatabaseSubReportService subReportService;
    private final JasperQrcodeGenerationService qrcodeGenerationService;
    private  final UtilityService utilityService ;
    private final .CustomFunctions

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
    public ResponseEntity<Object> verifyQrcode(String securityCode) {
        return qrcodeGenerationService.verifyQrcode(securityCode);
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
            entity.setExecutionTimeoutSeconds(domain.getExecutionTimeoutSeconds());
            entity.setReportFileName(domain.getReportFileName());
            entity.setDataSource(domain.getDataSource());
            entity.setReportContent(domain.getReportContent());
        }

        if (domain.getReportContent() == null
                || domain.getReportContent().length == 0) {
            throw new IllegalArgumentException(
                    "Report content is required");
        }

        JasperReportEntity updatedEntity = jasperReportRepository.save(entity);

        return jasperReportMapper.entityToDomain(updatedEntity);
    }

    @Override
    public ResponseEntity<JasperReportDomain> saveQrcodeInfo(JasperReportDomain domain) {
       try {
           if (domain.getId() == null) {
               throw new IllegalArgumentException("Report ID is required");
           }

           JasperReportEntity entity =
                   jasperReportRepository.findById(domain.getId())
                           .orElseThrow(() ->
                                   new RuntimeException(
                                           "Report not found with id: " + domain.getReportFileName()));

           if (domain.getReportFileName() == null
                   || domain.getReportFileName().trim().isEmpty()) {
               throw new IllegalArgumentException(
                       "Report file name is required");
           }

           entity.setQrcodeEnable(domain.isQrcodeEnable());
           entity.setSqlQueryForQrCode(utilityService.stringToClob(domain.getSqlQueryForQrCode()));
           entity.setMaxQrcodeScanLimit(domain.getMaxQrcodeScanLimit());
           entity.setDaysOfQrCodeExpiry(domain.getDaysOfQrCodeExpiry());
           jasperReportRepository.save(entity);
           return ResponseEntity.ok(domain);
       }catch (Exception e){
           throw new RuntimeException(e.getMessage());
       }

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
    public List<JasperReportDomain> getJasperReports(  Boolean isQrcodeEnable,
                                                       String reportFileName) {

        if (reportFileName != null && reportFileName.isBlank()) {
            reportFileName = null;
        }

        List<JasperReportEntity> entities =jasperReportRepository.findAllJasperReports(  isQrcodeEnable, reportFileName );

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
                        Date parsedDate = sdf.parse(dateValue);

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

    public static Map<String, Object> convertDateToString(
            Map<String, Object> parameters) {

        if (parameters == null) {
            return null;
        }

        Map<String, Object> result = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {

            Object value = entry.getValue();

            if (value instanceof Date) {
                result.put(entry.getKey(), sdf.format((Date) value));
            } else {
                result.put(entry.getKey(), value);
            }
        }

        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public ResponseEntity<byte[]> runReport(JasperReportRequestDomain request) throws IOException, JRException, SQLException {
        String reportFileName = request.getJasperFileName();
        JasperReportEntity entity = jasperReportRepository.findByReportFileName(reportFileName).get();
        JasperReportRequestDomain orginalRequest=request;
        orginalRequest.setParameters(convertDateToString(request.getParameters()));

        Integer defaultTimeoutSeconds = Integer.valueOf(AppConfig.getConfigurationValue("DEFAULT_REPORT_EXECUTION_TIMEOUT_SECOND"));
        String  qrcodeUrl= AppConfig.getConfigurationValue("QRCODE_URL_FOR_VALIDATE_DATA");

        String securityTokenForQrcode = null;

        Integer reportExecutionTimeout = entity.getExecutionTimeoutSeconds();

        int executionTimeoutSeconds =
                (reportExecutionTimeout != null && reportExecutionTimeout > 0)
                        ? reportExecutionTimeout
                        : defaultTimeoutSeconds;

        long auditLogId = 0;

        if (entity==null) {
            throw new RuntimeException("File not Found: " + reportFileName);
        }
        request.setParameters(convertDateParameters(request.getParameters()));

        if (entity.getReportContent() == null
                || entity.getReportContent().length == 0) {

            throw new RuntimeException(
                    "Jasper report content is empty: "
                            + reportFileName);
        }

        byte[] reportBytes = entity.getReportContent();


        // Get database connection

        DataSource dataSource = AppConfig.createDataSource(Long.valueOf(entity.getDataSource()));


        Clob qrCodeSql = entity.getSqlQueryForQrCode();

        if (qrCodeSql != null && qrCodeSql.length() > 0) {

            securityTokenForQrcode = qrcodeGenerationService.generateQrcode(
                    request.getParameters(),
                    entity,
                    dataSource,
                    auditLogId
            );
        }

        // Get parameters
        Map<String, Object> reportParameters = request.getParameters() != null
                ? new HashMap<>(request.getParameters())
                : new HashMap<>();

        if (securityTokenForQrcode != null && !securityTokenForQrcode.isBlank()) {
            reportParameters.put("PQR_CODE_URL", qrcodeUrl + securityTokenForQrcode);
        }


        try (Connection connection = dataSource.getConnection()) {

            // entry the initial log
            auditLogId = saveAuditData(orginalRequest, "PROCESSING", null, "",null);

            // String filePath = "C:\\Users\\User\\Downloads\\MITS_20028_CL_REPORT_OLD\\MITS_20028_CL_REPORT_OLD\\CL1_TOPSHEET_OLD.jasper";
            //File file = new File(filePath);
            //JasperReport jasperReport=JasperCompileManager.compileReport(filePath);
            // JasperReport jasperReport =(JasperReport) JRLoader.loadObject(file);

            // this is use for only compile main report
                 // JasperReport jasperReport = JasperCompileManager.compileReport(new ByteArrayInputStream(reportBytes));

            JasperReport jasperReport= subReportService.compileReport(
                    request.getJasperFileName(),
                    reportParameters
            );


           // JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportParameters, connection);

            // this is call for stop the execution if exceed the execution time;
            JasperPrint jasperPrint = fillReportWithTimeout(
                    jasperReport,
                    reportParameters,
                    connection,
                    executionTimeoutSeconds
            );

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
            if (securityTokenForQrcode != null && !securityTokenForQrcode.isBlank()) {
                qrcodeGenerationService.deletedQrcodeInfo(securityTokenForQrcode);
            }
            String errorMessage = ExceptionUtils.getFullErrorMessage(e);
            String errorShortMessage = ExceptionUtils.getRootErrorMessage(e);


            // Save audit log
            Long errorId = saveAuditData(
                    request,
                    "NOT-SUCCESS",
                    null,
                    errorMessage,
                    auditLogId
            );

            throw new ReportExecutionException(
                    errorShortMessage + " Error Log id: " + errorId
            );
        } catch (SQLException e) {
            if (securityTokenForQrcode != null && !securityTokenForQrcode.isBlank()) {
              qrcodeGenerationService.deletedQrcodeInfo(securityTokenForQrcode);
            }
            String errorMessage = ExceptionUtils.getFullErrorMessage(e);
            String errorShortMessage = ExceptionUtils.getRootErrorMessage(e);

            System.out.println("Root Cause: " + errorMessage);
            Long errorId = saveAuditData(
                    request,
                    "NOT-SUCCESS",
                    null,
                    errorMessage,
                    auditLogId
            );

            throw new ReportExecutionException(
                    errorShortMessage + " Error Log id: " + errorId
            );
        } catch (ReportExecutionException e) {
            if (securityTokenForQrcode != null && !securityTokenForQrcode.isBlank()) {
                qrcodeGenerationService.deletedQrcodeInfo(securityTokenForQrcode);
            }
            String errorMessage = ExceptionUtils.getFullErrorMessage(e);
            String errorShortMessage = ExceptionUtils.getRootErrorMessage(e);
            // Save timeout information
            saveAuditData(
                    request,
                    "TIMEOUT",
                    null,
                    e.getMessage(),
                    auditLogId
            );

            throw new ReportExecutionException(e.getMessage());

        }  catch (Exception e) {
            if (securityTokenForQrcode != null && !securityTokenForQrcode.isBlank()) {
                qrcodeGenerationService.deletedQrcodeInfo(securityTokenForQrcode);
            }
            String errorMessage = ExceptionUtils.getFullErrorMessage(e);
            String errorShortMessage = ExceptionUtils.getRootErrorMessage(e);

            System.out.println("Root Cause: " + errorMessage);
            Long errorId = saveAuditData(
                    request,
                    "NOT-SUCCESS",
                    null,
                    errorMessage,
                    auditLogId
            );

            throw new ReportExecutionException(
                    errorShortMessage + " Error Log id: " + errorId
            );
        }
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
        if (auditLogId != null && auditLogId>0) {
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

        entity.setEntryDate(new Date());
        entity.setReportFormat(request.getReportFormat());
        if(status=="PROCESSING"){
            entity.setParameter(convertMapToClob(request.getParameters()));
        }else{
            boolean isSaveReportData = Boolean.parseBoolean(
                    AppConfig.getConfigurationValue("IS_GENERATED_JASPER_REPORT_SAVE")
            );

            if (isSaveReportData) {
                entity.setReportData(reportData);
            }
        }


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

    public JasperPrint fillReportWithTimeout(
            JasperReport jasperReport,
            Map<String, Object> parameters,
            Connection connection,
            long timeoutSeconds) throws Exception {

        CountDownLatch completionLatch = new CountDownLatch(1);

        AtomicReference<JasperPrint> result = new AtomicReference<>();

        AtomicReference<Throwable> executionError = new AtomicReference<>();

        AtomicReference<Boolean> cancelled =
                new AtomicReference<>(false);

        AsynchronousFillHandle fillHandle = AsynchronousFillHandle.createHandle(
                        jasperReport,
                        parameters,
                        connection
                );

        fillHandle.addListener(new AsynchronousFilllListener() {

            @Override
            public void reportFinished(JasperPrint jasperPrint) {
                result.set(jasperPrint);
                completionLatch.countDown();
            }

            @Override
            public void reportFillError(Throwable throwable) {
                executionError.set(throwable);
                completionLatch.countDown();
            }

            @Override
            public void reportCancelled() {
                cancelled.set(true);
                completionLatch.countDown();
            }
        });

        try {

            // Start asynchronous report filling
            fillHandle.startFill();

            // Wait for the configured execution time
            boolean completed = completionLatch.await(
                    timeoutSeconds,
                    TimeUnit.SECONDS
            );

            // Time limit exceeded
            if (!completed) {

                // Cancel the current report and SQL execution
                fillHandle.cancellFill();

                // Wait briefly for cancellation notification
                completionLatch.await(5, TimeUnit.SECONDS);

                throw new ReportExecutionException (
                        "Report execution exceeded the maximum allowed time of "
                                + timeoutSeconds + " seconds. SQL execution was cancelled."
                );
            }

            // Check report execution errors
            if (executionError.get() != null) {
                throw new JRException(
                        "Report execution failed",
                        executionError.get()
                );
            }

            if (cancelled.get()) {
                throw new ReportExecutionException(
                        "Report execution was cancelled."
                );
            }

            return result.get();

        } catch (InterruptedException e) {

            fillHandle.cancellFill();

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Report execution was interrupted.",
                    e
            );
        }
    }


}