package DB;

import CSV.CsvReader;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class DBFunctions {

    private static final Logger loggerInfo = LoggerFactory.getLogger("com.info.logging");
    private static final Logger loggerDebug = LoggerFactory.getLogger("com.debug.logging");

    private static Boolean compareData(Integer newID, ArrayList<Integer> holdsID) {
        return holdsID.contains(newID);
    }

    public static class ColumnReader {
        private final File file;

        public ColumnReader(File file) {
            this.file = file;
        }

        public void readColumns(Map<Long, ArrayList<Integer>> idRemboursement_dict, Connection connection) throws CsvValidationException {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String headerLine = reader.readLine(); // Read the first line to get the column names
                String[] headers = headerLine.split(","); // Assuming columns are separated by commas (CSV)

                int numeroSecuriteSocialeIndex = findColumnIndex(headers, "Numero_Securite_Sociale");
                int idRemboursementIndex = findColumnIndex(headers, "ID_remboursement");

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(",");
                    if (values.length > Math.max(numeroSecuriteSocialeIndex, idRemboursementIndex)) {
                        String numeroSecuriteSociale = values[numeroSecuriteSocialeIndex];
                        String idRemboursement = values[idRemboursementIndex];

                        loggerInfo.info("Numero_Securite_Sociale: " + numeroSecuriteSociale);
                        loggerInfo.info("ID_remboursement: " + idRemboursement);

                        if (idRemboursement_dict.containsKey(Long.parseLong(numeroSecuriteSociale))) {
                            Boolean isDataMatching = compareData(Integer.parseInt(idRemboursement), idRemboursement_dict.get(Long.parseLong(numeroSecuriteSociale)));
                            loggerInfo.info("Data match result: " + isDataMatching);
                            DBLoad.updateRow(connection, "particuliers", line, CsvReader.getHeader(file), numeroSecuriteSociale, idRemboursement);
                        } else {
                            loggerInfo.info("No Secu not in DB");
                            DBLoad.insertRow(connection, "particuliers", line, CsvReader.getHeader(file));
                        }
                    }
                }
            } catch (IOException e) {
                loggerDebug.error("An error occurred while reading the CSV file.");
                e.printStackTrace();
            }
        }

        private int findColumnIndex(String[] headers, String columnName) {
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase(columnName)) {
                    return i;
                }
            }
            return -1; // Return -1 if the column is not found
        }
    }
}