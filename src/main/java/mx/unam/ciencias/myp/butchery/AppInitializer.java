package mx.unam.ciencias.myp.butchery;

import mx.unam.ciencias.myp.butchery.model.domain.Inventory;
import mx.unam.ciencias.myp.butchery.model.patrones.observer.FileNotifier;
import mx.unam.ciencias.myp.butchery.model.patrones.observer.ConsoleNotifier;

import java.io.File;
import java.io.IOException;

/**
 * Inicializador de la aplicación: configura recursos como el archivo de log
 * y registra observadores necesarios.
 */
public final class AppInitializer {

    private static final String LOG_FILE = "inventory_changes_log.txt";

    private AppInitializer() {}

    /**
     * Inicializa recursos de la aplicación. Trunca/crea el archivo de log
     * y registra observadores en el `Inventory`.
     */
    public static void init() {

        DatabaseInitializer.initialize();
        try {
            File f = new File(LOG_FILE);
            if (!f.exists()) {
                f.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("No se pudo preparar el archivo de log: " + e.getMessage());
        }

        try {
            Inventory inv = Inventory.getInstance();
            inv.register(new ConsoleNotifier());
            inv.register(new FileNotifier());
        } catch (Exception e) {
            System.err.println("No se pudieron registrar observadores: " + e.getMessage());
        }
        
    }
}
