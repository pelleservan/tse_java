package MAIN;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger loggerInfo = LoggerFactory.getLogger("com.info.logging");
    private static final Logger loggerDebug = LoggerFactory.getLogger("com.debug.logging");

    public static void main(String[] args) {

        loggerInfo.info("Batch processing started.");

        try {

            loggerDebug.debug("Reading CSV file...");

            loggerDebug.debug("Connecting to the database...");

            loggerDebug.debug("Inserting data into the database...");

            loggerInfo.info("Batch processed successfully.");
        } catch (Exception e) {
            loggerInfo.error("An error occurred during batch processing.", e);
        }

    }
}