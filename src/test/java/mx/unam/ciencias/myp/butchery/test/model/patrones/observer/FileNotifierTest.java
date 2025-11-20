package mx.unam.ciencias.myp.butchery.test.model.patrones.observer;
import mx.unam.ciencias.myp.butchery.model.patrones.observer.FileNotifier;
import org.junit.jupiter.api.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class FileNotifierTest {

    private static final String TEST_FILE = "test_inventory_log.txt";
    
    @BeforeEach
    public void setup() throws Exception {
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }
    
    @AfterEach
    public void cleanup() throws Exception {
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }
    

    @Test
    public void testUpdateWritesToFile() throws Exception {
        FileNotifier notifier = new FileNotifierTestHelper(TEST_FILE);

        notifier.update("Hola mundo");

        assertTrue(Files.exists(Paths.get(TEST_FILE)));

        String contenido = Files.readString(Paths.get(TEST_FILE));
        assertTrue(contenido.contains("Hola mundo"));
    }

    @Test
    public void testUpdateAppendsMessages() throws Exception {
        FileNotifier notifier = new FileNotifierTestHelper(TEST_FILE);

        notifier.update("Mensaje 1");
        notifier.update("Mensaje 2");

        String contenido = Files.readString(Paths.get(TEST_FILE));

        assertTrue(contenido.contains("Mensaje 1"));
        assertTrue(contenido.contains("Mensaje 2"));
    }


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
