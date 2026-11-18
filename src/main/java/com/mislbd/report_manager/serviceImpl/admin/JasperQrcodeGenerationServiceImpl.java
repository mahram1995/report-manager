package com.mislbd.report_manager.serviceImpl.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mislbd.report_manager.configuration.aopConfig.service.UtilityService;
import com.mislbd.report_manager.entity.admin.JasperReportEntity;
import com.mislbd.report_manager.entity.admin.JasperReportQrCodeInfo;
import com.mislbd.report_manager.repository.admin.JasperReportQrcodeInfoRepo;
import com.mislbd.report_manager.service.admin.JasperQrcodeGenerationService;
import com.mislbd.report_manager.utility.AppConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialClob;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Clob;
import java.util.*;

@Component
public class JasperQrcodeGenerationServiceImpl implements JasperQrcodeGenerationService {

    private final JasperReportQrcodeInfoRepo qrcodeInfoRepo;
    private final ObjectMapper objectMapper;
    private final UtilityService utilityService;


    public JasperQrcodeGenerationServiceImpl(JasperReportQrcodeInfoRepo qrcodeInfoRepo, ObjectMapper objectMapper, UtilityService utilityService) {
        this.qrcodeInfoRepo = qrcodeInfoRepo;
        this.objectMapper = objectMapper;
        this.utilityService = utilityService;
    }

    @Transactional
    public String generateQrcode(
            Object parameter,
            JasperReportEntity reportEntity,
            DataSource dataSource) {

        try {
            Integer  qrcodeExpiryDay= Integer.valueOf(AppConfig.getConfigurationValue("NUMBER_OF_DEFAULT_QRCODE_EXPIRY_DAY"));
            Integer  maxQrcodeScanLimit= Integer.valueOf(AppConfig.getConfigurationValue("MAX_QRCODE_SCAN_LIMIT"));

            // 1. Get configured SQL query
            Clob sqlClob = reportEntity.getSqlQueryForQrCode();

            if (sqlClob == null || sqlClob.length() == 0) {
                throw new RuntimeException(
                        "QR Code SQL query is not configured."
                );
            }

            String sqlQuery = sqlClob.getSubString(
                    1, Math.toIntExact(sqlClob.length())
            );

            // 2. Convert report parameters into Map
            Map<String, Object> reportParams =
                    objectMapper.convertValue(
                            parameter,
                            objectMapper.getTypeFactory()
                                    .constructMapType(
                                            Map.class,
                                            String.class,
                                            Object.class
                                    )
                    );

            // 3. Convert parameters to JSON
            String parameterJson =
                    objectMapper.writeValueAsString(reportParams);

            // 4. Generate UUID
            String uuid = UUID.randomUUID().toString();

            // 5. Generate security token
            String signature = generateSignature(
                    uuid + "|" + parameterJson
            );

            String securityToken = uuid + "." + signature;
            // 6. Execute configured SQL using provided datasource
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            List<Map<String, Object>> queryResult = jdbcTemplate.queryForList(sqlQuery, reportParams);

            // 7. Convert SQL result into JSON
            Object result = queryResult.isEmpty() ? null : queryResult.get(0);

            String jsonData = objectMapper.writeValueAsString(result);


            // 8. Create QR Code information entity
            JasperReportQrCodeInfo entity = new JasperReportQrCodeInfo();
            entity.setSecurityToken(securityToken);
            entity.setNumberOfScan(0);
            entity.setUuid(uuid);

            if(maxQrcodeScanLimit>reportEntity.getMaxQrcodeScanLimit()){
                entity.setScanLimit(maxQrcodeScanLimit);
            }else{
                entity.setScanLimit( reportEntity.getMaxQrcodeScanLimit());
            }
            entity.setExpiryDate(getQrcodeExpiryDate(qrcodeExpiryDay, reportEntity.getDaysOfQrCodeExpiry()));
            // Store SQL result JSON
            entity.setValue(new SerialClob(jsonData.toCharArray()));
            // Store report parameters
            entity.setParameters(new SerialClob(parameterJson.toCharArray()));
            // Store configured SQL
            entity.setSqlQueryForQrCode(new SerialClob(sqlQuery.toCharArray()));

            // 9. Save into database
            qrcodeInfoRepo.save(entity);

            // 10. Return security token
            return securityToken;

        } catch (Exception e) {

            throw new RuntimeException("Failed to generate QR Code Data: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deletedQrcodeInfo(String securityToken) {
        qrcodeInfoRepo.deleteBySecurityToken(securityToken);
    }

    @Override
    public ResponseEntity<Object> verifyQrcode(String securityToken) {

        try {

            if (securityToken == null || securityToken.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Security token is required."));
            }

            String[] tokenParts = securityToken.split("\\.", -1);

            if (tokenParts.length != 2) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Invalid security token."));
            }

            String uuid = tokenParts[0];

            JasperReportQrCodeInfo  entity = qrcodeInfoRepo.findByUuid(uuid);

            if (entity == null) {
                return ResponseEntity.status(404)
                        .body(Map.of("message", "QR Code not found."));
            }
           // JasperReportQrCodeInfo entity=qrcodeInfoRepo.findBySecurityToken(securityToken);

            if (!uuid.equals(entity.getUuid())) {
                throw new RuntimeException(
                        "QR Code signature is invalid or tampered."
                );
            }

            if (entity != null) {
                Clob clob = entity.getValue();
                if (clob == null) {
                    return ResponseEntity.notFound().build();
                }

                // Retrieve original JSON from database
                String parameterJson = utilityService.clobToJson( entity.getParameters());

                // Verify signature
                boolean valid = verifySignature(
                        securityToken,
                        parameterJson
                );

                if (!valid) {
                    return ResponseEntity.status(403)
                            .body(Map.of("message", "QR Code signature is invalid or tampered."));
                }

                String value = clob.getSubString(1, (int) clob.length());
                if(entity.getNumberOfScan()==entity.getScanLimit()){
                    throw new RuntimeException("Scan Limit exceed");
                }
                if (entity.getExpiryDate() != null
                        && entity.getExpiryDate().compareTo(new Date()) < 0) {

                    throw new RuntimeException("QR Code is expired");
                }
                entity.setNumberOfScan(entity.getNumberOfScan()+1);
                qrcodeInfoRepo.save(entity);
                return ResponseEntity.ok(value);

            } else {

                throw new RuntimeException("Security Code is Invalid. Please enter correct security code");

            }



        } catch (Exception e) {
             throw new RuntimeException( e.getMessage());
        }
    }

    public Date getQrcodeExpiryDate(
            Integer defaultQrcodeExpiryDay,
            Integer reportLevelExpiryDay) {

        int expiryDays = Math.max(
                defaultQrcodeExpiryDay != null ? defaultQrcodeExpiryDay : 0,
                reportLevelExpiryDay != null ? reportLevelExpiryDay : 0
        );

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, expiryDays);

        return calendar.getTime();
    }

    private String generateSignature(String data) throws Exception {
        String  qrCodeSecret= AppConfig.getConfigurationValue("QRCODE_SECURITY_SECRET");

        Mac mac = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKey = new SecretKeySpec(
                qrCodeSecret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        mac.init(secretKey);

        byte[] hash = mac.doFinal(
                data.getBytes(StandardCharsets.UTF_8)
        );

        return HexFormat.of().formatHex(hash);
    }


    private boolean verifySignature(
            String securityToken,
            String parameterJson
    ) throws Exception {

        if (securityToken == null || parameterJson == null) {
            return false;
        }

        String[] tokenParts = securityToken.split("\\.", -1);

        // Token must contain exactly UUID.SIGNATURE
        if (tokenParts.length != 2) {
            return false;
        }

        String uuid = tokenParts[0];
        String receivedSignature = tokenParts[1];

        // Validate UUID
        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException ex) {
            return false;
        }

        // Recalculate signature
        String data = uuid + "|" + parameterJson;

        String expectedSignature = generateSignature(data);

        try {
            byte[] receivedBytes = HexFormat.of().parseHex(receivedSignature);
            byte[] expectedBytes = HexFormat.of().parseHex(expectedSignature);

            // Constant-time comparison
            return MessageDigest.isEqual(expectedBytes, receivedBytes);

        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
