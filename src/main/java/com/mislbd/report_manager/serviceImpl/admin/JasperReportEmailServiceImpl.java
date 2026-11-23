package com.mislbd.report_manager.serviceImpl.admin;
import com.mislbd.report_manager.service.admin.JasperReportEmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JasperReportEmailServiceImpl implements JasperReportEmailService {
    private final JavaMailSender mailSender;
    @Value("${app.mail.from}")
    private String fromEmail;
    @Override
    public boolean sendReport(
            String to,
            String cc,
            boolean sendSeparately,
            String subject,
            String body,
            String signature,
            String fileName,
            byte[] reportData
    ) throws Exception {

        if (reportData == null || reportData.length == 0) {
            throw new IllegalArgumentException(
                    "Generated report is empty. Email cannot be sent."
            );
        }

        List<String> recipients = parseEmails(to);
        List<String> ccRecipients = null;
        if (cc != null && !cc.isBlank()) {
            ccRecipients = parseEmails(cc);
        }

        if (recipients.isEmpty()) {
            throw new IllegalArgumentException(
                    "Email recipient is not configured."
            );
        }

        String emailSubject = (subject == null || subject.isBlank())
                ? "Scheduled Report"
                : subject;

        String emailBody = buildBody(body, signature);

        if (sendSeparately) {

            // One email per To recipient
            for (String recipient : recipients) {
                sendSingleEmail(
                        recipient,
                        ccRecipients,
                        emailSubject,
                        emailBody,
                        fileName,
                        reportData
                );
            }
            return true;

        } else {

            // One email with all To recipients
            sendSingleEmail(
                    recipients.toArray(new String[0]),
                    ccRecipients,
                    emailSubject,
                    emailBody,
                    fileName,
                    reportData
            );
            return true;
        }
    }

    private void sendSingleEmail(
            String to,
            List<String> cc,
            String subject,
            String body,
            String fileName,
            byte[] reportData
    ) throws Exception {

        sendSingleEmail(
                new String[]{to},
                cc,
                subject,
                body,
                fileName,
                reportData
        );
    }


    private void sendSingleEmail(
            String[] to,
            List<String> cc,
            String subject,
            String body,
            String fileName,
            byte[] reportData
    ) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(
                message,
                true,
                StandardCharsets.UTF_8.name()
        );

        helper.setTo(to);

        if (cc != null && !cc.isEmpty()) {
            helper.setCc(cc.toArray(new String[0]));
        }

        helper.setFrom(fromEmail);
        helper.setSubject(subject);
        helper.setText(body, true);

        helper.addAttachment(
                fileName,
                new ByteArrayResource(reportData),
                getContentType(fileName)
        );

        mailSender.send(message);
    }

    private List<String> parseEmails(String emails) {

        List<String> result = new ArrayList<>();

        if (emails == null || emails.isBlank()) {
            return result;
        }

        for (String email : emails.split("[,;]")) {

            String value = email.trim();

            if (!value.isEmpty()) {
                result.add(value);
            }
        }

        return result;
    }

    private String buildBody(String body, String signature) {

        StringBuilder html = new StringBuilder();

        if (body != null && !body.isBlank()) {
            html.append(body.replace("\n", "<br>"));
        }

        if (signature != null && !signature.isBlank()) {

            if (!html.isEmpty()) {
                html.append("<br><br>");
            }

            html.append(signature.replace("\n", "<br>"));
        }

        return html.toString();
    }

    private String getContentType(String fileName) {

        if (fileName == null) {
            return "application/octet-stream";
        }

        String lower = fileName.toLowerCase();

        if (lower.endsWith(".pdf")) {
            return "application/pdf";
        }

        if (lower.endsWith(".xlsx")) {
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        }

        if (lower.endsWith(".xls")) {
            return "application/vnd.ms-excel";
        }

        if (lower.endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }

        if (lower.endsWith(".csv")) {
            return "text/csv";
        }

        return "application/octet-stream";
    }
}
