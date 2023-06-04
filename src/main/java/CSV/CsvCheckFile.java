package CSV;

import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class CsvCheckFile {

    private static final Logger loggerInfo = LoggerFactory.getLogger("com.info.logging");
    private static final Logger loggerDebug = LoggerFactory.getLogger("com.debug.logging");

    public static boolean checkHeader(List<String> fileHeader, List<String> mandatoryHeader) {
        if (fileHeader.size() != mandatoryHeader.size()) {
            return false;
        }

        Set<String> set1 = new HashSet<>(fileHeader);
        Set<String> set2 = new HashSet<>(mandatoryHeader);

        return set1.equals(set2);
    }

    public static Class<?> getDataType(String inputData) {
        if (inputData.equalsIgnoreCase("true") || inputData.equalsIgnoreCase("false")) {
            return Boolean.class;
        } else {
            try {
                int number = Integer.parseInt(inputData);
                return Integer.class;
            } catch (NumberFormatException e1) {
                try {
                    long number = Long.parseLong(inputData);
                    return Long.class;
                } catch (NumberFormatException e2) {
                    try {
                        float number = Float.parseFloat(inputData);
                        return Float.class;
                    } catch (NumberFormatException e3) {
                        try {
                            double number = Double.parseDouble(inputData);
                            return Double.class;
                        } catch (NumberFormatException e4) {
                            return String.class;
                        }
                    }
                }

            }
        }
    }

    public static boolean validateCSV(File csvFile, File outputFolderPath, ArrayList<String > fileHeader, Map<String, String> dataType, Map<String, String> dataPattern) throws CsvValidationException  {
        String line;
        String cvsSplitBy = ",";
        String[] columns = fileHeader.toArray(new String[fileHeader.size()]);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();

            // Iterate through each line of the CSV
            while ((line = br.readLine()) != null) {
                String[] dataLine = line.split(",");

                String lineState = "True";
                // Check each column defined in the data type dictionary
                for (Map.Entry<String, String> entry : dataType.entrySet()) {
                    String columnName = entry.getKey();
                    String columnType = dataType.get(columnName);

                    int columnIndex = getColumnIndex(columns, columnName);

                    if (columnIndex == -1) {
                        loggerInfo.error("Column '" + columnName + "' does not exist in the CSV file.");
                        lineState = "False";
                    }

                    String columnValue = dataLine[columnIndex];

                    // Check data type if defined
                    if (columnType != null && !columnValue.isEmpty()) {
                        if (!validateColumnType(columnValue, columnType)) {
                            loggerInfo.error("Data '" + columnValue + "' in column '" + columnName +
                                    "' does not match the type '" + columnType + "'.");
                            lineState = "False";
                        }
                    }

                    if (dataPattern.containsKey(columnName)) {
                        // Check data pattern if defined
                        String columnPattern = dataPattern.get(columnName);
                        if (columnPattern != null && !columnValue.isEmpty()) {
                            if (!validateColumnPattern(columnValue, columnPattern)) {
                                loggerInfo.error("Data '" + columnValue + "' in column '" + columnName +
                                        "' does not match the pattern '" + columnPattern + "'.");
                                lineState = "False";
                            }
                        }
                    }
                }
                CsvWriter.outputCsv(lineState, line, outputFolderPath, csvFile);
            }

            // All lines have been successfully validated
            return true;
        } catch (IOException e) {
            loggerDebug.error("An error occurred while processing the CSV file: " + e.getMessage());
            return false;
        }
    }

    private static int getColumnIndex(String[] columns, String columnName) {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].equals(columnName)) {
                return i;
            }
        }
        return -1; // Column not found
    }

    private static boolean validateColumnType(String value, String type) {
        if (type.equals("float")) {
            if (getDataType(value).equals(Float.class)) {
                return true;
            } else {
                return false;
            }
        } else if (type.equals("int")) {
            if (getDataType(value).equals(Integer.class)) {
                return true;
            } else {
                return false;
            }
        } else if (type.equals("long")) {
            if (getDataType(value).equals(Long.class)) {
                return true;
            } else {
                return false;
            }
        } else if (type.equals("string")) {
            if (getDataType(value).equals(String.class)) {
                return true;
            } else {
                return false;
            }
        } else if (type.equals("double")) {
            if (getDataType(value).equals(Double.class)) {
                return true;
            } else {
                return false;
            }
        } else if (type.equals("boolean")) {
            if (getDataType(value).equals(Boolean.class)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean validateColumnPattern(String value, String pattern) {
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(value);
        return matcher.matches();
    }
}