package DB;

import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.File;

import java.util.ArrayList;
import java.util.Map;

public class DBConnection {

    private static final Logger loggerInfo = LoggerFactory.getLogger("com.info.logging");
    private static final Logger loggerDebug = LoggerFactory.getLogger("com.debug.logging");

    public static void connectToDB(File file) throws CsvValidationException {
        String jdbcURL = "jdbc:mysql://localhost:3306/java_project";
        String username = "root";
        String password = "rootpswd";

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);

            if (connection != null) {
                loggerInfo.info("Successfully connected to the database!");

                // Perform operations on the database...
                Map<Long, ArrayList<Integer>> idRemboursement = DBImport.loadDataFromDB(connection);
                DBFunctions.ColumnReader columnReader = new DBFunctions.ColumnReader(file);
                columnReader.readColumns(idRemboursement, connection);

                // Close the connection
                connection.close();
            }
        } catch (ClassNotFoundException e) {
            loggerDebug.error("Error: JDBC driver failed to load!");
            e.printStackTrace();
        } catch (SQLException e) {
            loggerDebug.error("Error: Unable to connect to the database!");
            e.printStackTrace();
        }
    }
}