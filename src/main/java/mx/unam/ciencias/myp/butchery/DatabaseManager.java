package mx.unam.ciencias.myp.butchery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:data/butchery.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC"); // Carga del driver
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar el driver SQLite", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
