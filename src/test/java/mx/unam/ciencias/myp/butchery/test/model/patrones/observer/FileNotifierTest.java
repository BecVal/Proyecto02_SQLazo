package mx.unam.ciencias.myp.butchery.test.model.patrones.observer;
import mx.unam.ciencias.myp.butchery.model.patrones.observer.FileNotifier;
import org.junit.jupiter.api.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link FileNotifier}, encargada de escribir mensajes en un archivo de texto como parte del patrón Observer.
 * <p>
 * 
 * Este conjunto de pruebas valida que:
 * <ul>
 *     <li>Se cree correctamente el archivo cuando llega la primera notificación.</li>
 *     <li>Los mensajes se escriban dentro del archivo.</li>
 *     <li>Las llamadas múltiples a {@code update()} agreguen mensajes sin sobrescribir contenido previo.</li>
 * </ul>
 * </p>
 */
public class FileNotifierTest {

    private static final String TEST_FILE = "test_inventory_log.txt";

    /**
     * Se ejecuta antes de cada prueba.
     * 
     * Asegura que el archivo de pruebas no exista para comenzar desde un estado limpio.
     */
    @BeforeEach

    public void setup() throws Exception {
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    /**
     * 
     * Se ejecuta al finalizar cada prueba.
     * Limpia el archivo generado para no dejar residuos en el directorio de trabajo.
     */
    @AfterEach
    public void cleanup() throws Exception {
        Files.deleteIfExists(Paths.get(TEST_FILE));

    }
    /**
     * 
     * Verifica que la llamada a {@link FileNotifier#update(String)}:
     * <ul>
     *     <li>cree el archivo si no existía previamente,</li>
     *     <li>y escriba correctamente el mensaje recibido.</li>
     * 
     * </ul>
     */
    @Test
    public void testUpdateWritesToFile() throws Exception {

        FileNotifier notifier = new FileNotifierTestHelper(TEST_FILE);
        notifier.update("Hola mundo");

        assertTrue(Files.exists(Paths.get(TEST_FILE)));

        String contenido = Files.readString(Paths.get(TEST_FILE));
        assertTrue(contenido.contains("Hola mundo"));
    }

    /**
     * Verifica que múltiples invocaciones a {@link FileNotifier#update(String)} agreguen mensajes al archivo sin sobrescribir los existentes.
     */
    @Test
    public void testUpdateAppendsMessages() throws Exception {

        FileNotifier notifier = new FileNotifierTestHelper(TEST_FILE);

        notifier.update("Mensaje 1");
        notifier.update("Mensaje 2");

        String contenido = Files.readString(Paths.get(TEST_FILE));
        assertTrue(contenido.contains("Mensaje 1"));
        assertTrue(contenido.contains("Mensaje 2"));
    }

    /**
     * Clase auxiliar que permite cambiar dinámicamente el nombre del archivo utilizado por
     * {@link FileNotifier}, usando reflexión para modificar el atributo privado {@code filename}.
     * <p>
     * Esto evita tener que modificar el código original y permite realizar pruebas aisladas.
     * </p>
     */
    private static class FileNotifierTestHelper extends FileNotifier {
        public FileNotifierTestHelper(String filename) {

            try {
                var field = FileNotifier.class.getDeclaredField("filename");
                field.setAccessible(true);
                field.set(this, filename);
            } catch (Exception e) {

                throw new RuntimeException(e);
            }
        }

    }

}