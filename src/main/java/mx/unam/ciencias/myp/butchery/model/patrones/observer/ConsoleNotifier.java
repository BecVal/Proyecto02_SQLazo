package mx.unam.ciencias.myp.butchery.model.patrones.observer;
/**
 * Observador que imprime en consola los mensajes enviados por el sujeto que esté siendo observado.
 *
 * <p>Este notifier se utiliza para mostrar al usuario de la aplicación cualquier cambio registrado en el inventario u otros eventos relevantes siguiendo el patrón de diseño Observer.</p>
 */
public class ConsoleNotifier implements Observer {
    /**
     * Muestra en consola el mensaje recibido desde el sujeto observado.
     * @param message mensaje que indica el evento ocurrido.
     */
    @Override
    public void update(String message) {

        System.out.println("[CONSOLE] " + message);
    }
}

