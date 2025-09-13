package com.mislbd.report_manager.controller.report;

import com.mislbd.report_manager.configuration.aopConfig.entity.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @GetMapping("/get-report")
    public ResponseEntity<?> getReport(HttpServletResponse response,
                                       @RequestParam(name = "reportName", required = false) String reportName,
                                       @RequestParam(name = "parameter", required = false) String parameter,
                                       @RequestParam(name = "reportType", required = false) String reportType) {

        String jasperUrl = "http://localhost:8080/jasperserver/rest_v2/reports/reports/report/"
                + reportName + "." + reportType + "?" + parameter;

        try {
            System.out.println("Calling Jasper URL: " + jasperUrl);

            URL url = new URL(jasperUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int statusCode = conn.getResponseCode();

            // 🔹 Handle 404 explicitly → return text/plain as Blob
            if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
                response.setContentType("text/plain");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);

                response.getOutputStream().write(("Report file not found: " + reportName).getBytes());
                return null;
            }

            // 🔹 Handle other Jasper errors
            if (statusCode != HttpURLConnection.HTTP_OK) {
                String errorMessage="SQL error";


                response.setContentType("text/plain");
                response.setStatus(statusCode);
                response.getOutputStream().write(errorMessage.getBytes());
                return null;
            }

            // 🔹 Success → stream file
            response.setContentType(getMimeType(reportType));
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", "inline; filename=" + reportName + "." + reportType);

            try (InputStream in = conn.getInputStream(); OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[8192];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
            }

            return null; // already streamed

        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.setContentType("text/plain");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getOutputStream().write(("Server error: " + e.getMessage()).getBytes());
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
            return null;
        }
    }




    // helper method to resolve content type
    private String getMimeType(String reportType) {
        switch (reportType.toLowerCase()) {
            case "pdf":
                return "application/pdf";
            case "txt":
                return "text/plain";
            case "html":
                return "text/html";
            case "xls":
            case "xlsx":
                return "application/vnd.ms-excel";
            case "csv":
                return "text/csv";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "rtf":
                return "application/rtf";
            case "odt":
                return "application/vnd.oasis.opendocument.text";
            case "ods":
                return "application/vnd.oasis.opendocument.spreadsheet";
            default:
                return "application/octet-stream";
        }
    }




}
