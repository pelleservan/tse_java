import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvMain {

    private static final Logger loggerInfo = LoggerFactory.getLogger("com.info.logging");

    public static void main(String[] args) throws CsvValidationException {

        loggerInfo.info("Batch launched");

        // Get csv input file format
        File csvFormatFile = new File("./src/main/resources/format/define_header.xml");

        // Import csv input file format into dictionary
        Map<String, Map<String, String>> dictionary = CsvFormat.xlmToMap(csvFormatFile);

        // Get mandatory headers
        ArrayList<String> mandatoryHeaders = CsvFormat.getMandatoryHeaders(dictionary);

        StringBuilder mandatoryHeadersConcat = new StringBuilder();

        for (String header : mandatoryHeaders) {
            mandatoryHeadersConcat.append(header).append(" ");
        }

        String mandatoryHeadersConcatenated = mandatoryHeadersConcat.toString();

        System.out.println("Mandatory headers: " + mandatoryHeadersConcatenated);

        // Get patterns for file name
        List<String> patterns = CsvFormat.getPatternsFromXml(csvFormatFile);

        StringBuilder patternConcat = new StringBuilder();

        for (String pattern : patterns) {
            patternConcat.append(pattern).append(" ");
        }

        String patternConcatenated = patternConcat.toString();

        System.out.println("\nFile name pattern : " + patternConcatenated);

        // Get data type
        Map<String, String> dataType = CsvFormat.getDataType(dictionary);

        System.out.println("\nData type : ");
        for (Map.Entry<String, String> entry : dataType.entrySet()) {
            String clé = entry.getKey();
            String valeur = entry.getValue();
            System.out.println(clé + " : " + valeur);
        }

        // Get data pattern
        Map<String, String> dataPattern = CsvFormat.getDataPattern(dictionary);

        System.out.println("\nData patterns :");
        for (Map.Entry<String, String> entry : dataPattern.entrySet()) {
            String clé = entry.getKey();
            String valeur = entry.getValue();
            System.out.println(clé + " : " + valeur);
        }

        // Get input files
        File inputFolder = new File("./src/main/resources/input_particulier");

        // Get list of files with matching names
        ArrayList<File> csvFiles = CsvReader.readFolderContent(inputFolder, patterns);

        for (File file : csvFiles) {
            ArrayList<String> headers = CsvReader.getHeader(file);

            StringBuilder headerConcat = new StringBuilder();

            for (String header : headers) {
                headerConcat.append(header).append(" ");
            }

            String headersConcatenated = headerConcat.toString();

            // Check headers file correspond to mandatory specifications
            boolean headerChecked = CsvCheckFile.checkHeader(headers, mandatoryHeaders);

            System.out.println("File: " + file + "\nHeaders: " + headersConcatenated +"\nHeader checked : " + headerChecked);

            if (headerChecked) {
                File outputFolder = new File("./src/main/resources/output_particulier");
                CsvCheckFile.validateCSV(file, outputFolder, CsvReader.getHeader(file), dataType, dataPattern);
            }
        }

    }
}
