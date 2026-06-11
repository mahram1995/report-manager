package com.mislbd.report_manager.controller.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:4200") // Your Angular app URL
public class ReportProxyController {
    private static final String JASPER_BASE = "http://localhost:8080/jasperserver/rest_v2";
    private static final String JASPER_USER = "jasperadmin";
    private static final String JASPER_PASS = "jasperadmin";

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/dynamic-db")
    public ResponseEntity<?> generateReportWithDynamicDB(
            @RequestParam(name = "reportName", defaultValue = "ACCOUNT_BALANCE_REPORT") String reportName,
            @RequestParam(name = "reportType", defaultValue = "pdf") String reportType,
            @RequestParam(name = "parameter", required = false) String parameter
    ) {
        try {
            // 1️⃣ Build request body
            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("reportUnitUri", "/reports/report/" + reportName);
            requestBody.put("async", false);

            // Parameters (optional)
            Map<String, Object> params = new HashMap<>();
            if (parameter != null && !parameter.isEmpty()) {
                for (String p : parameter.split(",")) {
                    String[] kv = p.split("=", 2);
                    if (kv.length == 2)
                        params.put(kv[0].trim(), kv[1].trim());
                }
            }
            requestBody.put("parameters", params);

            // Dynamic JDBC datasource
            Map<String, Object> ds = new HashMap<>();
            ds.put("type", "jdbc");
            ds.put("driverClass", "oracle.jdbc.OracleDriver");
            ds.put("url", "jdbc:oracle:thin:@localhost:1521:xe");
            ds.put("username", "hims");
            ds.put("password", "root");

            requestBody.put("dataSource", ds);;

            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBasicAuth(JASPER_USER, JASPER_PASS);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 2️⃣ Start report execution
            String execUrl = JASPER_BASE + "/reportExecutions";
            ResponseEntity<Map> startResp = restTemplate.exchange(execUrl, HttpMethod.POST, entity, Map.class);
            Map<?, ?> startBody = startResp.getBody();

            if (startBody == null || startBody.get("requestId") == null)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Missing requestId from JasperServer: " + startBody);

            String requestId = startBody.get("requestId").toString();
            System.out.println("✅ Request ID: " + requestId);

            // 3️⃣ Wait until ready
            for (int i = 0; i < 10; i++) {
                ResponseEntity<Map> statusResp = restTemplate.exchange(
                        execUrl + "/" + requestId,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        Map.class
                );
                Map<?, ?> status = statusResp.getBody();
                System.out.println("Status: " + status);

                if (status != null && "ready".equalsIgnoreCase((String) status.get("status"))) {
                    break;
                }
                TimeUnit.SECONDS.sleep(1);
            }

            // 4️⃣ Trigger an export explicitly
            Map<String, Object> exportRequest = new HashMap<>();
            exportRequest.put("outputFormat", reportType.toLowerCase());
          //  exportRequest.put("pages", "1-1000"); // optional

            HttpEntity<Map<String, Object>> exportEntity = new HttpEntity<>(exportRequest, headers);

            ResponseEntity<Map> exportResp = restTemplate.exchange(
                    execUrl + "/" + requestId + "/exports",
                    HttpMethod.POST,
                    exportEntity,
                    Map.class
            );

            Map<?, ?> exportBody = exportResp.getBody();
            System.out.println("📄 Export Response: " + exportBody);

            if (exportBody == null || exportBody.get("id") == null)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to export report: " + exportBody);

            String exportId = exportBody.get("id").toString();

            // 5️⃣ Download the exported resource
            String downloadUrl = execUrl + "/" + requestId + "/exports/" + exportId + "/outputResource";

            ResponseEntity<byte[]> fileResp = restTemplate.exchange(
                    downloadUrl,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    byte[].class
            );

            if (!fileResp.getStatusCode().is2xxSuccessful())
                return ResponseEntity.status(fileResp.getStatusCode())
                        .body("Failed to download report: " + fileResp.getStatusCode());

            MediaType mt = reportType.equalsIgnoreCase("pdf")
                    ? MediaType.APPLICATION_PDF
                    : MediaType.APPLICATION_OCTET_STREAM;

            return ResponseEntity.ok()
                    .contentType(mt)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + reportName + "." + reportType)
                    .body(fileResp.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}