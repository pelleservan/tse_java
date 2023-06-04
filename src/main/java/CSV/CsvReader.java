package CSV;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class CsvReader {

    private static final Logger loggerInfo = LoggerFactory.getLogger("com.info.logging");
    private static final Logger loggerDebug = LoggerFactory.getLogger("com.debug.logging");

    public static ArrayList<File> readFolderContent(File directory, List<String> fileNameFormats) {
        ArrayList<File> csvFiles = new ArrayList<>();

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && matchesFileNameFormat(file.getName(), fileNameFormats)) {
                        csvFiles.add(file);
                        loggerInfo.info("Found CSV file: " + file.getName());
                    }
                }
            }
        }

        return csvFiles;
    }

    private static boolean matchesFileNameFormat(String fileName, List<String> fileNameFormats) {
        for (String fileNameFormat : fileNameFormats) {
            if (fileName.matches(fileNameFormat)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> getHeader(File file) throws CsvValidationException {
        ArrayList<String> headers = new ArrayList<>();

        if (file.isFile() && file.getName().endsWith(".csv")) {
            try {
                loggerInfo.info("Reading header from CSV file: " + file.getName());

                FileReader filereader = new FileReader(file);
                CSVReader csvReader = new CSVReader(filereader);

                String[] headersArray = csvReader.readNext();
                Collections.addAll(headers, headersArray);

                loggerInfo.info("Header read successfully: " + headers);
            } catch (IOException e) {
                loggerDebug.error("Error reading CSV file header: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            loggerDebug.debug("Skipping file: " + file.getName() + " as it is not a CSV file.");
        }

        return headers;
    }
}