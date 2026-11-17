
package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.entity.admin.JasperReportEntity;
import com.mislbd.report_manager.exception.ExceptionUtils;
import com.mislbd.report_manager.repository.admin.JasperReportRepository;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.w3c.dom.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.util.*;

@Service
public class DatabaseSubReportService {

    private final JasperReportRepository jasperReportRepository;

    private static final String PARAM_PREFIX =
            "__DB_SUBREPORT__";

    private static final String IMAGE_PARAM_PREFIX =
            "__DB_IMAGE__";

    public DatabaseSubReportService(
            JasperReportRepository jasperReportRepository) {

        this.jasperReportRepository = jasperReportRepository;
    }

    /**
     * Compile main report and all nested subreports.
     */
    @Transactional(readOnly = true)
    public JasperReport compileReport(
            String mainReportFileName,
            Map<String, Object> parameters) throws Exception {

            if (parameters == null) {
                throw new IllegalArgumentException(
                        "Report parameters cannot be null"
                );
            }

            Map<String, JasperReport> compiledReports =
                    new HashMap<>();

            Set<String> processingReports =
                    new HashSet<>();

            // Compile main report and nested subreports.
            JasperReport mainReport = loadAndCompile(
                    mainReportFileName,
                    compiledReports,
                    processingReports,
                    parameters
            );

            // Make compiled subreports available to the main report.
            for (Map.Entry<String, JasperReport> entry : compiledReports.entrySet()) {
                parameters.put(PARAM_PREFIX + entry.getKey(), entry.getValue());
            }

            return mainReport;


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

    /**
     * Load JRXML from database and compile it.
     */
    private JasperReport loadAndCompile(
            String reportFileName,
            Map<String, JasperReport> compiledReports,
            Set<String> processingReports,
            Map<String, Object> parameters) throws Exception {

        if (compiledReports.containsKey(reportFileName)) {
            return compiledReports.get(reportFileName);
        }

        if (!processingReports.add(reportFileName)) {
            throw new IllegalStateException(
                    "Circular subreport reference detected: "
                            + reportFileName
            );
        }

        try {

            JasperReportEntity entity =
                    jasperReportRepository
                            .findByReportFileName(reportFileName)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Report not found in database: "
                                                    + reportFileName
                                    )
                            );

            byte[] jrxmlData = entity.getReportContent();

            if (jrxmlData == null || jrxmlData.length == 0) {
                throw new RuntimeException(
                        "Empty JRXML content: " + reportFileName
                );
            }

            // Process images and subreports.
            byte[] modifiedJrxml = processJrxml(
                    jrxmlData,
                    compiledReports,
                    processingReports,
                    parameters
            );

            JasperReport compiledReport;

            try (InputStream inputStream =
                         new ByteArrayInputStream(modifiedJrxml)) {

                compiledReport =
                        JasperCompileManager.compileReport(inputStream);
            }

            compiledReports.put(
                    reportFileName,
                    compiledReport
            );

            return compiledReport;

        } finally {
            processingReports.remove(reportFileName);
        }
    }

    /**
     * Process images and subreports inside JRXML.
     */
    private byte[] processJrxml(
            byte[] jrxmlData,
            Map<String, JasperReport> compiledReports,
            Set<String> processingReports,
            Map<String, Object> parameters) throws Exception {

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

        factory.setNamespaceAware(true);

        // Secure XML parsing.
        factory.setFeature(
                "http://apache.org/xml/features/disallow-doctype-decl",
                true
        );

        factory.setFeature(
                "http://xml.org/sax/features/external-general-entities",
                false
        );

        factory.setFeature(
                "http://xml.org/sax/features/external-parameter-entities",
                false
        );

        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);

        factory.setAttribute(
                XMLConstants.ACCESS_EXTERNAL_DTD,
                ""
        );

        factory.setAttribute(
                XMLConstants.ACCESS_EXTERNAL_SCHEMA,
                ""
        );

        Document document;

        try (InputStream inputStream =
                     new ByteArrayInputStream(jrxmlData)) {

            document = factory.newDocumentBuilder().parse(inputStream);
        }

        Element root = document.getDocumentElement();

        // =========================================
        // PROCESS IMAGES
        // =========================================

        NodeList images =
                document.getElementsByTagNameNS("*", "image");

        for (int i = 0; i < images.getLength(); i++) {

            Element image = (Element) images.item(i);

            Element imageExpression = getDirectChild(
                    image,
                    "imageExpression"
            );

            if (imageExpression == null) {
                continue;
            }

            String expressionText = imageExpression.getTextContent().trim().replace("repo:", "");

            // Extract static image filename.
            String imageFileName = expressionText
                    .replace("\"", "")
                    .replace("'", "")
                    .trim();

            // Skip dynamic expressions.
            if (imageFileName.isEmpty()
                    || imageFileName.contains("$P{")
                    || imageFileName.contains("$F{")
                    || imageFileName.contains("$V{")
                    || imageFileName.contains("(")
                    || imageFileName.contains("+")) {

                continue;
            }

            // Only process image files.
            String lowerName =
                    imageFileName.toLowerCase(Locale.ROOT);

            if (!(lowerName.endsWith(".png")
                    || lowerName.endsWith(".jpg")
                    || lowerName.endsWith(".jpeg")
                    || lowerName.endsWith(".gif")
                    || lowerName.endsWith(".bmp")
                    || lowerName.endsWith(".tif")
                    || lowerName.endsWith(".tiff"))) {

                continue;
            }

            String parameterName =
                    IMAGE_PARAM_PREFIX + imageFileName;

            // Retrieve image from database only once.
            if (!parameters.containsKey(parameterName)) {

                JasperReportEntity imageEntity =
                        jasperReportRepository
                                .findByReportFileName(imageFileName)
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Image not found in database: "
                                                        + imageFileName
                                        )
                                );

                byte[] imageData =
                        imageEntity.getReportContent();

                if (imageData == null || imageData.length == 0) {
                    throw new RuntimeException(
                            "Empty image data: " + imageFileName
                    );
                }

                // Pass raw byte array.
                parameters.put(
                        parameterName,
                        imageData
                );
            }

            // Dynamically declare image parameter.
            addParameterIfNotExists(
                    document,
                    root,
                    parameterName,
                    "java.lang.Object"
            );

            // Replace original image expression.
            imageExpression.setTextContent(
                    "$P{" + parameterName + "}"
            );
        }

        // =========================================
        // PROCESS SUBREPORTS
        // =========================================

        NodeList subreports =
                document.getElementsByTagNameNS("*", "subreport");

        for (int i = 0; i < subreports.getLength(); i++) {

            Element subreport =
                    (Element) subreports.item(i);

            Element expression = getDirectChild(
                    subreport,
                    "subreportExpression"
            );

            if (expression == null) {
                continue;
            }

            String expressionText =
                    expression.getTextContent().trim();

            // Remove surrounding quotes.
            String subreportFileName = expressionText
                    .replace("\"", "")
                    .replace("'", "")
                    .trim();

            // Remove repo: prefix.
            if (subreportFileName.regionMatches(
                    true, 0, "repo:", 0, 5)) {

                subreportFileName =
                        subreportFileName.substring(5).trim();
            }

            if (!subreportFileName.toLowerCase(Locale.ROOT)
                    .endsWith(".jrxml")) {

                continue;
            }

            // Recursively compile subreport.
            loadAndCompile(
                    subreportFileName,
                    compiledReports,
                    processingReports,
                    parameters
            );

            String parameterKey =
                    PARAM_PREFIX + subreportFileName;

            // Declare subreport parameter if required.
            addParameterIfNotExists(
                    document,
                    root,
                    parameterKey,
                    "net.sf.jasperreports.engine.JasperReport"
            );

            // Replace subreport expression.
            expression.setTextContent(
                    "$P{REPORT_PARAMETERS_MAP}.get(\""
                            + parameterKey + "\")"
            );

            // Pass parent parameter map to child.
            Element parameterMapExpression = getDirectChild(
                    subreport,
                    "parametersMapExpression"
            );

            if (parameterMapExpression == null) {

                parameterMapExpression =
                        document.createElement("parametersMapExpression");

                Element firstParameter = getDirectChild(
                        subreport,
                        "subreportParameter"
                );

                if (firstParameter != null) {

                    subreport.insertBefore(
                            parameterMapExpression,
                            firstParameter
                    );

                } else {

                    subreport.insertBefore(
                            parameterMapExpression,
                            expression
                    );
                }
            }

            parameterMapExpression.setTextContent(
                    "$P{REPORT_PARAMETERS_MAP}"
            );
        }

        // =========================================
        // CONVERT XML TO BYTE ARRAY
        // =========================================

        TransformerFactory transformerFactory =
                TransformerFactory.newInstance();

        transformerFactory.setFeature(
                XMLConstants.FEATURE_SECURE_PROCESSING,
                true
        );

        transformerFactory.setAttribute(
                XMLConstants.ACCESS_EXTERNAL_DTD,
                ""
        );

        transformerFactory.setAttribute(
                XMLConstants.ACCESS_EXTERNAL_STYLESHEET,
                ""
        );

        Transformer transformer =
                transformerFactory.newTransformer();

        transformer.setOutputProperty(
                OutputKeys.ENCODING,
                "UTF-8"
        );

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        transformer.transform(
                new DOMSource(document),
                new StreamResult(outputStream)
        );

        return outputStream.toByteArray();
    }

    /**
     * Add parameter declaration in correct JRXML position.
     */

    private void addParameterIfNotExists(
            Document document,
            Element root,
            String parameterName,
            String parameterClass) {

        NodeList parameters =
                root.getElementsByTagNameNS("*", "parameter");

        // Check whether parameter already exists.
        for (int i = 0; i < parameters.getLength(); i++) {

            Element parameter = (Element) parameters.item(i);

            if (parameterName.equals(parameter.getAttribute("name"))) {
                return;
            }
        }

        // Create parameter using the same namespace.
        String namespace = root.getNamespaceURI();
        String prefix = root.getPrefix();

        String qualifiedName =
                prefix == null || prefix.isEmpty()
                        ? "parameter"
                        : prefix + ":parameter";

        Element newParameter = document.createElementNS(
                namespace,
                qualifiedName
        );

        newParameter.setAttribute("name", parameterName);
        newParameter.setAttribute("class", parameterClass);

        // Find last existing parameter.
        Element lastParameter = null;

        NodeList children = root.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {

            Node child = children.item(i);

            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Element element = (Element) child;

            String localName = element.getLocalName();

            if ("parameter".equals(localName)) {
                lastParameter = element;
            }
        }

        if (lastParameter != null) {

            // Insert immediately after last parameter.
            Node next = lastParameter.getNextSibling();

            root.insertBefore(newParameter, next);

        } else {

            // No existing parameters.
            // Insert before the first field or later section.
            Node insertBefore = null;

            for (int i = 0; i < children.getLength(); i++) {

                Node child = children.item(i);

                if (child.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                String name = child.getLocalName();

                if ("field".equals(name)
                        || "sortField".equals(name)
                        || "variable".equals(name)
                        || "filterExpression".equals(name)
                        || "group".equals(name)
                        || "background".equals(name)
                        || "title".equals(name)
                        || "pageHeader".equals(name)
                        || "columnHeader".equals(name)
                        || "detail".equals(name)
                        || "columnFooter".equals(name)
                        || "pageFooter".equals(name)
                        || "lastPageFooter".equals(name)
                        || "summary".equals(name)
                        || "noData".equals(name)) {

                    insertBefore = child;
                    break;
                }
            }

            if (insertBefore != null) {
                root.insertBefore(newParameter, insertBefore);
            } else {
                root.appendChild(newParameter);
            }
        }
    }


    /**
     * Find direct child element by local name.
     */
    private Element getDirectChild(
            Element parent,
            String childName) {

        NodeList children = parent.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {

            Node node = children.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                String localName = node.getLocalName();

                if (localName == null) {
                    localName = node.getNodeName();
                }

                if (childName.equals(localName)) {
                    return (Element) node;
                }
            }
        }

        return null;
    }
}
