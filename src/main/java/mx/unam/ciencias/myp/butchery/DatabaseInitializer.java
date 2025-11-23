package mx.unam.ciencias.myp.butchery;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

                String sqlInventory =
                "CREATE TABLE IF NOT EXISTS inventory (" +
                "id TEXT PRIMARY KEY, " +
                "name TEXT UNIQUE NOT NULL, " +
                "price REAL NOT NULL, " +
                "stock REAL NOT NULL, " +
                "type TEXT NOT NULL" +
                ");";
        

            stmt.execute(sqlInventory);

        } catch (Exception e) {
            throw new RuntimeException("No se pudo inicializar la base de datos", e);
        }
    }
}
