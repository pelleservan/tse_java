package CSV;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvFormat {

    private static final Logger loggerInfo = LoggerFactory.getLogger("com.info.logging");
    private static final Logger loggerDebug = LoggerFactory.getLogger("com.debug.logging");

    public static Map<String, Map<String, String>> xlmToMap(File xmlFile) {
        Map<String, Map<String, String>> dictionary = new HashMap<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            Element csvformatElement = doc.getDocumentElement();
            NodeList fieldsNodeList = csvformatElement.getElementsByTagName("field");

            for (int i = 0; i < fieldsNodeList.getLength(); i++) {
                Node fieldNode = fieldsNodeList.item(i);

                if (fieldNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element fieldElement = (Element) fieldNode;

                    String fieldName = fieldElement.getAttribute("name");
                    String fieldType = fieldElement.getAttribute("type");
                    String fieldRequired = fieldElement.getAttribute("required");
                    String fieldSize = fieldElement.getAttribute("size");
                    String fieldPattern = fieldElement.getAttribute("pattern");

                    Map<String, String> fieldProperties = new HashMap<>();
                    fieldProperties.put("type", fieldType);
                    fieldProperties.put("required", fieldRequired);
                    fieldProperties.put("size", fieldSize);
                    fieldProperties.put("pattern", fieldPattern);

                    dictionary.put(fieldName, fieldProperties);
                }
            }
        } catch (Exception e) {
            loggerDebug.error("Error occurred while parsing XML file: " + e.getMessage());
        }

        return dictionary;
    }

    public static List<String> getPatternsFromXml(File xmlFilePath) {
        List<String> patterns = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(xmlFilePath);

            doc.getDocumentElement().normalize();

            NodeList formatNodeList = doc.getElementsByTagName("src/format");

            for (int i = 0; i < formatNodeList.getLength(); i++) {
                Node formatNode = formatNodeList.item(i);

                if (formatNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element formatElement = (Element) formatNode;
                    String format = formatElement.getTextContent().trim();
                    patterns.add(format);
                }
            }
        } catch (Exception e) {
            loggerDebug.error("Error occurred while parsing XML file: " + e.getMessage());
        }

        return patterns;
    }

    public static ArrayList<String> getMandatoryHeaders(Map<String, Map<String, String>> dictionary) {
        ArrayList<String> mandatoryHeaders = new ArrayList<>();

        for (Map.Entry<String, Map<String, String>> entry : dictionary.entrySet()) {
            Map<String, String> fieldProperties = entry.getValue();
            if (fieldProperties.get("required").equals("true")) {
                mandatoryHeaders.add(entry.getKey());
            }
        }

        return mandatoryHeaders;
    }

    public static Map<String, String> getDataType(Map<String, Map<String, String>> dictionary) {
        Map<String, String> dataFormat = new HashMap<>();

        for (Map.Entry<String, Map<String, String>> entry : dictionary.entrySet()) {
            Map<String, String> fieldProperties = entry.getValue();
            String type = fieldProperties.get("type");
            if (fieldProperties.get("required").equals("true")) {
                dataFormat.put(entry.getKey(), type);
            }
        }

        return dataFormat;
    }

    public static Map<String, String> getDataPattern(Map<String, Map<String, String>> dictionary) {
        Map<String, String> dataPattern = new HashMap<>();

        for (Map.Entry<String, Map<String, String>> entry : dictionary.entrySet()) {
            Map<String, String> fieldProperties = entry.getValue();
            String pattern = fieldProperties.get("pattern");
            if (pattern != null && !pattern.isEmpty()) {
                dataPattern.put(entry.getKey(), pattern);
            }
        }

        return dataPattern;
    }
}