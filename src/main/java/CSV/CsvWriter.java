package CSV;

import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CsvWriter {

    private static final Logger loggerInfo = LoggerFactory.getLogger("com.info.logging");
    private static final Logger loggerDebug = LoggerFactory.getLogger("com.debug.logging");

    public static void outputCsv(String state, String line, File outputFolderPath, File inputFile) throws CsvValidationException {
        File csvFilePath = new File("");
        String path;

        if (state.equals("True")) {
            path = inputFile.getPath();

            int lastSeparatorIndex = path.lastIndexOf(File.separator);
            if (lastSeparatorIndex != -1) {
                String newFileName = outputFolderPath + "/clean_" + path.substring(lastSeparatorIndex + 1);
                csvFilePath = new File(newFileName);
            }
        } else {
            path = inputFile.getPath();

            int lastSeparatorIndex = path.lastIndexOf(File.separator);
            if (lastSeparatorIndex != -1) {
                String newFileName = outputFolderPath + "/dirty_" + path.substring(lastSeparatorIndex + 1);
                csvFilePath = new File(newFileName);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, true))) {
            if (csvFilePath.length() == 0) {
                ArrayList<String> headers = CsvReader.getHeader(inputFile);
                String headersString = String.join(",", headers);
                loggerInfo.info("Writing headers to CSV file: " + csvFilePath.getName());
                writer.write(headersString);
                writer.newLine();
            }

            writer.write(line);
            writer.newLine();

            loggerInfo.info("Line written to CSV file: " + csvFilePath.getName());
        } catch (IOException e) {
            loggerDebug.error("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}