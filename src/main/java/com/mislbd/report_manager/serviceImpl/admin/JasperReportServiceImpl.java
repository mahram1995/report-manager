package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.domain.admin.JasperReportDomain;
import com.mislbd.report_manager.domain.admin.JasperReportRequestDomain;
import com.mislbd.report_manager.entity.admin.DatabaseConfigEntity;
import com.mislbd.report_manager.entity.admin.JasperReportEntity;
import com.mislbd.report_manager.entity.admin.JasperReportHistEntity;
import com.mislbd.report_manager.exception.ReportExecutionException;
import com.mislbd.report_manager.mapper.admin.JasperReportMapper;
import com.mislbd.report_manager.repository.admin.DatabaseConfigRepository;
import com.mislbd.report_manager.repository.admin.JasperReportHistRepository;
import com.mislbd.report_manager.repository.admin.JasperReportRepository;
import com.mislbd.report_manager.service.admin.JasperReportService;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JasperReportServiceImpl implements JasperReportService {

    private final JasperReportRepository jasperReportRepository;
    private  final JasperReportHistRepository histRepository;
    private final JasperReportMapper jasperReportMapper;
    private  final DatabaseConfigRepository databaseConfigRepository;

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

            Optional<JasperReportEntity> entity=jasperReportRepository.findByReportFileName(domain.getReportFileName());
            JasperReportHistEntity hisEntity= jasperReportMapper.EntityToHistEntity(entity);

            histRepository.save(hisEntity);
            return   update(entity.get().getId(), domain);
        }

        JasperReportEntity entity = jasperReportMapper.domainToEntity(domain);

        JasperReportEntity savedEntity = jasperReportRepository.save(entity);

        return jasperReportMapper.entityToDomain(savedEntity);
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
    public List<JasperReportDomain> getByJasperReposts() {
        List<JasperReportEntity> entities = jasperReportRepository.findAll();

        return entities.stream().map(jasperReportMapper::entityToDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional()
    public     ResponseEntity<byte[]> runReport(JasperReportRequestDomain request) throws IOException, JRException {
        String reportFileName=request.getJasperFileName();
        Optional<JasperReportEntity> entity = jasperReportRepository.findByReportFileName(reportFileName);

        if(entity.isEmpty()){
            throw new RuntimeException("File not Found: " + reportFileName);
        }

        if (entity.get().getReportContent() == null
                || entity.get().getReportContent().length == 0) {

            throw new RuntimeException(
                    "Jasper report content is empty: "
                            + reportFileName);
        }

        byte[] reportBytes = entity.get().getReportContent();




        // Get database connection
        DatabaseConfigEntity db = getDatabase(101L).orElseThrow(() -> new RuntimeException("Database not found"));
        DataSource dataSource = createDataSource(db);
        // Get parameters
        Map<String, Object> reportParameters = request.getParameters() != null
                        ? new HashMap<>(request.getParameters())
                        : new HashMap<>();


        try (Connection connection = dataSource.getConnection()) {

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

                return ResponseEntity.ok()
                        .contentType(
                                MediaType.TEXT_HTML
                        )
                        .body(outputStream.toByteArray());
            }

            return ResponseEntity.ok(JasperExportManager.exportReportToPdf(jasperPrint));

        } catch (JRException e) {

            Throwable cause = e;

            while (cause.getCause() != null) {
                cause = cause.getCause();
            }

            throw new ReportExecutionException(
                    cause.getMessage(),
                    e
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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



}