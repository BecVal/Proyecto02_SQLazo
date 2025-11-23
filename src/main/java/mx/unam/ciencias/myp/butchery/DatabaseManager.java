package mx.unam.ciencias.myp.butchery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * Gestiona las conexiones a la base de datos SQLite utilizadas por la aplicación.
 *
 * <p>
 * Esta clase actúa como un punto centralizado para obtener conexiones JDBC hacia el archivo <strong>butchery.db</strong> ubicado en el directorio <code>data/</code>.
 * Se encarga de cargar el driver de SQLite y de proveer métodos seguros para abrir conexiones cuando otras clases del sistema lo requieran.
 * </p>
 *
 * 
 * <p>
 * Su implementación es completamente estática, ya que no es necesario crear instancias de esta clase. Esto garantiza simplicidad y eficiencia al manejar la base de datos.
 * 
 * </p>
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:data/butchery.db";

    /**
     * Bloque estático que carga el driver JDBC de SQLite al inicio de la aplicación.
     *
     * <p>
     * La llamada a <code>Class.forName</code> garantiza que el driver esté registrado correctamente en el {@link DriverManager}, permitiendo que se puedan solicitar conexiones más adelante.
     * </p>
     *
     * @throws RuntimeException si el driver no puede cargarse.
     */
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar el driver SQLite", e);
        }
    }

    /**
     * 
     * Obtiene una nueva conexión hacia la base de datos SQLite.
     * <p>
     * Cada llamada devuelve una conexión independiente. Es responsabilidad del código cliente cerrar la conexión cuando ya no la necesite.
     * </p>
     *
     * @return una conexión activa hacia la base de datos.
     * @throws SQLException si ocurre un error al abrir la conexión.
     */
    public static Connection getConnection() throws SQLException {
        
        return DriverManager.getConnection(DB_URL);
    }
}
