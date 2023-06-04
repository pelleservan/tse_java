package CSV;

import FILE.FileMover;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CsvMain {

    private static final Logger loggerInfo = LoggerFactory.getLogger("com.info.logging");
    private static final Logger loggerDebug = LoggerFactory.getLogger("com.debug.logging");

    public static void main() throws CsvValidationException {

        // Get csv input file format
        File csvFormatFile = new File("./src/format/define_header.xml");

        loggerInfo.info("Reading CSV input file format from: " + csvFormatFile.getPath());

        // Import csv input file format into dictionary
        Map<String, Map<String, String>> dictionary = CsvFormat.xlmToMap(csvFormatFile);

        loggerInfo.info("CSV input file format imported successfully.");

        // Get mandatory headers
        ArrayList<String> mandatoryHeaders = CsvFormat.getMandatoryHeaders(dictionary);

        loggerInfo.info("Mandatory headers obtained: " + mandatoryHeaders);

        // Get patterns for file name
        List<String> patterns = CsvFormat.getPatternsFromXml(csvFormatFile);

        loggerInfo.info("Patterns for file name obtained: " + patterns);

        // Get data type
        Map<String, String> dataType = CsvFormat.getDataType(dictionary);

        loggerInfo.info("Data types obtained: " + dataType);

        // Get data pattern
        Map<String, String> dataPattern = CsvFormat.getDataPattern(dictionary);

        loggerInfo.info("Data patterns obtained: " + dataPattern);

        // Get input files
        File inputFolder = new File("./src/data/input_particulier");
        File processed = new File("./src/data/processed_files");

        loggerInfo.info("Reading input files from: " + inputFolder.getPath());

        // Get list of files with matching names
        ArrayList<File> csvFiles = CsvReader.readFolderContent(inputFolder, patterns);

        loggerInfo.info("Found " + csvFiles.size() + " input files.");

        for (File file : csvFiles) {
            loggerInfo.info("Processing file: " + file.getName());

            ArrayList<String> headers = CsvReader.getHeader(file);

            // Check headers file correspond to mandatory specifications
            boolean headerChecked = CsvCheckFile.checkHeader(headers, mandatoryHeaders);

            if (headerChecked) {
                loggerInfo.info("Header check passed for file: " + file.getName());

                File outputFolder = new File("./src/data/output_particulier");

                CsvCheckFile.validateCSV(file, outputFolder, headers, dataType, dataPattern);

                loggerInfo.info("CSV file validated and processed successfully: " + file.getName());
            }

            FileMover.moveFileToDirectory(file, processed);

            loggerInfo.info("Moved processed file: " + file.getName() + " to directory: " + processed.getPath());
        }
    }
}