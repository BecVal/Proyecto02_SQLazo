package mx.unam.ciencias.myp.butchery.test.model.patrones.observer;
import mx.unam.ciencias.myp.butchery.model.patrones.observer.ListNotifier;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ListNotifierTest {

    @Test
    public void testUpdateAddsMessagesToLog() {
        ListNotifier notifier = new ListNotifier();

        notifier.update("Mensaje 1");
        notifier.update("Mensaje 2");

        List<String> log = notifier.getLog();

        assertEquals(2, log.size());
        assertEquals("Mensaje 1", log.get(0));
        assertEquals("Mensaje 2", log.get(1));
    }

    @Test
    public void testLogStartsEmpty() {
        ListNotifier notifier = new ListNotifier();

        assertTrue(notifier.getLog().isEmpty());
    }

    @Test
    public void testGetLogReturnsLiveList() {
        ListNotifier notifier = new ListNotifier();

        notifier.update("Hola");

        List<String> log = notifier.getLog();
        log.add("Modificacion externa");

        assertEquals(2, notifier.getLog().size());
        assertEquals("Modificacion externa", notifier.getLog().get(1));
    }
}
