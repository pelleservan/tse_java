package DB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class DBLoad {

    private static final Logger loggerInfo = LoggerFactory.getLogger("com.info.logging");
    private static final Logger loggerDebug = LoggerFactory.getLogger("com.debug.logging");

    public static void insertRow(Connection connection, String tableName, String rowData, ArrayList<String> columnNames) {
        try {
            StringBuilder columnNamesStr = new StringBuilder();
            StringBuilder columnValues = new StringBuilder();
            List<String> values = new ArrayList<>(List.of(rowData.split(",")));

            if (columnNames.size() != values.size()) {
                throw new IllegalArgumentException("Number of column names does not match the number of values");
            }

            for (String columnName : columnNames) {
                if (columnNamesStr.length() > 0) {
                    columnNamesStr.append(", ");
                    columnValues.append(", ");
                }
                columnNamesStr.append(columnName);
                columnValues.append("?");
            }

            String sql = "INSERT INTO " + tableName + " (" + columnNamesStr + ") VALUES (" + columnValues + ")";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                loggerInfo.info("Inserting a new row into table " + tableName);

                // Set the parameter values for the prepared statement
                for (int i = 0; i < values.size(); i++) {
                    statement.setString(i + 1, values.get(i));
                }

                // Execute the SQL statement
                statement.executeUpdate();
                loggerInfo.info("Row inserted successfully into table " + tableName);
            }
        } catch (SQLException e) {
            loggerDebug.error("An error occurred while inserting a row into table " + tableName);
            e.printStackTrace();
            // Handle the exception or rethrow it if necessary
        }
    }

    public static void updateRow(Connection connection, String tableName, String rowData, ArrayList<String> columnNames, String numeroSecuriteSociale, String idRemboursement) {
        try {
            StringBuilder setClause = new StringBuilder();
            List<String> values = new ArrayList<>(List.of(rowData.split(",")));

            if (columnNames.size() != values.size()) {
                throw new IllegalArgumentException("Number of column names does not match the number of values");
            }

            for (String columnName : columnNames) {
                if (setClause.length() > 0) {
                    setClause.append(", ");
                }
                setClause.append(columnName).append(" = ?");
            }

            String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE Numero_Securite_Sociale = ? AND ID_remboursement = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                loggerInfo.info("Updating a row in table " + tableName);

                // Set the parameter values for the prepared statement
                int parameterIndex = 1;
                for (String value : values) {
                    statement.setString(parameterIndex++, value);
                }
                statement.setString(parameterIndex++, numeroSecuriteSociale);
                statement.setString(parameterIndex, idRemboursement);

                // Execute the SQL statement
                statement.executeUpdate();
                loggerInfo.info("Row updated successfully in table " + tableName);
            }
        } catch (SQLException e) {
            loggerDebug.error("An error occurred while updating a row in table " + tableName);
            e.printStackTrace();
            // Handle the exception or rethrow it if necessary
        }
    }
}