package DB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DBImport {

    private static final Logger loggerInfo = LoggerFactory.getLogger("com.info.logging");
    private static final Logger loggerDebug = LoggerFactory.getLogger("com.debug.logging");

    public static Map<Long, ArrayList<Integer>> loadDataFromDB(Connection conn) {
        String query = "SELECT * FROM particuliers";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Map<Long, ArrayList<Integer>> id_remboursment_dict = new HashMap<>();

            while (rs.next()) {
                long numero_securite_sociale = rs.getLong("NUMERO_SECURITE_SOCIALE");
                int id_remboursement = rs.getInt("ID_REMBOURSEMENT");

                if (id_remboursment_dict.containsKey(numero_securite_sociale)) {
                    id_remboursment_dict.get(numero_securite_sociale).add(id_remboursement);
                } else {
                    ArrayList<Integer> remboursements = new ArrayList<>();
                    remboursements.add(id_remboursement);
                    id_remboursment_dict.put(numero_securite_sociale, remboursements);
                }

                loggerInfo.info("NUMERO_SECURITE_SOCIALE: " + numero_securite_sociale + ", ID_REMBOURSEMENT: " + id_remboursement);
            }

            loggerInfo.info("Loaded data from the MySQL database: " + id_remboursment_dict);

            rs.close(); // Close the ResultSet
            stmt.close(); // Close the Statement

            loggerInfo.info("Data loaded successfully from the MySQL database.");
            return id_remboursment_dict;
        } catch (SQLException e) {
            loggerDebug.error("An error occurred while loading data from the MySQL database.");
            e.printStackTrace();
            return null;
        }
    }
}