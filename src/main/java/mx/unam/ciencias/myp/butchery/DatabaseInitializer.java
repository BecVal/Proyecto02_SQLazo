package mx.unam.ciencias.myp.butchery;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Inicializa la base de datos utilizada por la aplicación.
 * <p>
 * Esta clase se encarga de crear las tablas necesarias para el funcionamiento del sistema si aún no existen. Su propósito es garantizar que la base de datos SQLite tenga la estructura mínima requerida antes de que cualquier componente del modelo intente interactuar con ella.
 * 
 * </p>
 * <p>
 * 
 * Actualmente gestiona la creación de la tabla <strong>inventory</strong>, donde se almacena la información de los productos registrados en la carnicería, incluyendo su identificador, nombre, precio, cantidad en existencia y tipo.
 * </p>
 */
public class DatabaseInitializer {

    /**
     * Ejecuta la inicialización de la base de datos.
     *
     * <p>
     * Este método abre una conexión a la base de datos, crea un {@link Statement} y ejecuta la sentencia SQL que define la tabla <strong>inventory</strong> si aún no está creada.
     * </p>
     * <p>
     * En caso de que ocurra algún error durante este proceso, se lanza una {@link RuntimeException} para indicar que la base de datos no pudo ser inicializada correctamente.
     * </p>
     */
    public static void initialize() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            String sqlInventory = "CREATE TABLE IF NOT EXISTS inventory (" +
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
