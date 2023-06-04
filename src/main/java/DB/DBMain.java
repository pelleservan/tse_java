package DB;

import FILE.FileMover;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class DBMain {

    private static final Logger loggerInfo = LoggerFactory.getLogger("com.info.logging");
    private static final Logger loggerDebug = LoggerFactory.getLogger("com.debug.logging");

    public static void main() throws CsvValidationException {

        File processed = new File("./src/main/resources/processed_files");

        File clearDataCSV = new File("./src/main/resources/output_particulier");
        File[] files = clearDataCSV.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().matches("clean_users_[0-9]{12}.csv")) {
                    loggerInfo.info("Processing file: " + file.getName());
                    // Process the CSV file
                    DBConnection.connectToDB(file);
                    // Call the method to read the contents of the CSV file here
                }
                FileMover.moveFileToDirectory(file, processed);
                loggerInfo.info("Moved file " + file.getName() + " to the processed_files directory");
            }
        }
    }
}