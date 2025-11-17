package mx.unam.ciencias.myp.butchery.model.patrones.observer;

/**
 * Interfaz que define el comportamiento de un observador dentro del patrón de diseño Observer.
 *
 * <p>Las clases que implementen esta interfaz podrán recibir notificaciones cuando ocurra un evento en el sujeto observado. Cada observador decide cómo manejar el mensaje recibido.</p>
 */
public interface Observer {
    /**
     * Método llamado por el sujeto observado para notificar un evento.
     * @param message mensaje que describe el cambio ocurrido.
     * 
     */

    void update(String message);
}