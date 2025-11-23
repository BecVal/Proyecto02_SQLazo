package mx.unam.ciencias.myp.butchery.test.model.patrones.observer;

import mx.unam.ciencias.myp.butchery.model.patrones.observer.ListNotifier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Pruebas unitarias para la clase {@link ListNotifier}, un observador que registra los mensajes recibidos en una lista en memoria.
 * <p>
 * Estas pruebas verifican que:
 * <ul>
 *     <li>Los mensajes se agreguen correctamente al log interno.</li>
 *     <li>El log comience vacío.</li>
 * 
 *     <li>La lista devuelta por {@link ListNotifier#getLog()} sea la misma lista interna y permita modificaciones externas (comportamiento esperado en esta implementación).</li>
 * </ul>
 * 
 * </p>
 */
public class ListNotifierTest {
    /**
     * 
     * Verifica que cada llamada a {@link ListNotifier#update(String)} agregue el mensaje recibido al log interno en el orden correcto.
     */
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

    /**
     * Verifica que un {@link ListNotifier} recién creado inicie con un log vacío.
     * 
     */
    @Test

    public void testLogStartsEmpty() {

        ListNotifier notifier = new ListNotifier();

        assertTrue(notifier.getLog().isEmpty());
    }

    /**
     * Verifica que {@link ListNotifier#getLog()} devuelva una referencia directa a la lista interna, permitiendo modificaciones externas.
     * <p>
     * Este comportamiento no es común en diseños inmutables, pero es intencional en esta clase para facilitar pruebas y depuración.
     * </p>
     */
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
