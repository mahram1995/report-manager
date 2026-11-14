package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.entity.admin.JasperReportEntity;
import com.mislbd.report_manager.repository.admin.JasperReportRepository;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DatabaseSubReportService {

    private final JasperReportRepository jasperReportRepository;

    private static final String PARAM_PREFIX = "__DB_SUBREPORT__";

    private static final Pattern REPO_PATTERN = Pattern.compile(
            "(?i)repo:([^\"']+?\\.jrxml)"
    );

    public DatabaseSubReportService(JasperReportRepository jasperReportRepository) {
        this.jasperReportRepository = jasperReportRepository;
    }


    /**
     * Load and compile main report and all its subreports.
     */
    @Transactional(readOnly = true)
    public JasperReport compileReport(
            String mainReportFileName,
            Map<String, Object> parameters) throws Exception {

        Map<String, JasperReport> compiledReports =
                new HashMap<>();

        Set<String> processingReports = new HashSet<>();

        // Compile main report and recursively compile subreports.
        JasperReport mainReport = loadAndCompile(
                mainReportFileName,
                compiledReports,
                processingReports
        );

        // Make every compiled subreport available to the main report.
        for (Map.Entry<String, JasperReport> entry :
                compiledReports.entrySet()) {

            parameters.put(
                    PARAM_PREFIX + entry.getKey(),
                    entry.getValue()
            );
        }

        return mainReport;
    }

    /**
     * Load JRXML from database, process nested subreports,
     * and compile the report.
     */
    private JasperReport loadAndCompile(
            String reportFileName,
            Map<String, JasperReport> compiledReports,
            Set<String> processingReports) throws Exception {

        // Already compiled during this execution.
        if (compiledReports.containsKey(reportFileName)) {
            return compiledReports.get(reportFileName);
        }

        // Prevent circular subreport references.
        if (!processingReports.add(reportFileName)) {
            throw new IllegalStateException( "Circular subreport reference detected: " + reportFileName );
        }

        try {
            JasperReportEntity entity = jasperReportRepository
                    .findByReportFileName(reportFileName)
                    .orElseThrow(() -> new RuntimeException(
                            "Report not found in database: "
                                    + reportFileName
                    ));

            byte[] jrxmlData = entity.getReportContent();

            if (jrxmlData == null || jrxmlData.length == 0) {
                throw new RuntimeException(  "Empty JRXML content: " + reportFileName  );
            }

            // Modify JRXML in memory.
            byte[] modifiedJrxml = processSubreports(
                    jrxmlData,
                    compiledReports,
                    processingReports
            );

            // Compile the modified JRXML.
            JasperReport compiledReport;

            try (InputStream inputStream =new ByteArrayInputStream(modifiedJrxml)) {

                compiledReport =  JasperCompileManager.compileReport(inputStream);
            }

            compiledReports.put(   reportFileName,compiledReport  );

            return compiledReport;

        } finally {
            processingReports.remove(reportFileName);
        }
    }

    /**
     * Find all subreports and replace their expressions.
     */

    private byte[] processSubreports(
            byte[] jrxmlData,
            Map<String, JasperReport> compiledReports,
            Set<String> processingReports) throws Exception {

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

        factory.setNamespaceAware(true);

        // Prevent XML external entity processing.
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

        Document document;

        try (InputStream inputStream =
                     new ByteArrayInputStream(jrxmlData)) {

            document = factory.newDocumentBuilder().parse(inputStream);
        }

        NodeList subreports =
                document.getElementsByTagNameNS("*", "subreport");

        // Process every subreport.
        for (int i = 0; i < subreports.getLength(); i++) {

            Element subreport = (Element) subreports.item(i);

            Element expression = getDirectChild(
                    subreport,
                    "subreportExpression"
            );

            if (expression == null) {
                continue;
            }

            // Read original expression.
            String expressionText =
                    expression.getTextContent().trim();

            System.out.println(
                    "Original Subreport Expression: " + expressionText
            );

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

            // Remove any remaining whitespace.
            subreportFileName = subreportFileName.trim();

            // Only process JRXML files.
            if (!subreportFileName.toLowerCase(Locale.ROOT)
                    .endsWith(".jrxml")) {

                System.out.println(
                        "Skipping non-JRXML expression: "
                                + expressionText
                );

                continue;
            }

            System.out.println(
                    "Resolved Subreport File: " + subreportFileName
            );

            // Load and compile subreport recursively.
            loadAndCompile(
                    subreportFileName,
                    compiledReports,
                    processingReports
            );

            // Create parameter key.
            String parameterKey =
                    PARAM_PREFIX + subreportFileName;

            // Replace expression with compiled report reference.
            expression.setTextContent(
                    "$P{REPORT_PARAMETERS_MAP}.get(\""
                            + parameterKey + "\")"
            );

            /*
             * Pass the parent parameter map to the child report.
             * This allows nested subreports to access compiled
             * subreports and inherited parameters.
             */
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

        // Convert modified XML back to bytes.
        TransformerFactory transformerFactory =
                TransformerFactory.newInstance();

        transformerFactory.setFeature(
                javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING,
                true
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
     * Find a direct child element by its local name.
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
