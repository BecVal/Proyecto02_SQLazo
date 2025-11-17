package mx.unam.ciencias.myp.butchery.model.patrones.observer;

import java.util.ArrayList;

import java.util.List;

/**
 * Observador que almacena en memoria todos los mensajes notificados por el sujeto.
 *
 * <p>Este notifier mantiene un registro interno de los eventos observados en una lista, permitiendo recuperar posteriormente todo el historial de mensajes generados.
 * Es útil para depuración, pruebas o para mostrar un registro acumulado dentro de la aplicación.</p>
 *
 * <p>Forma parte de la implementación del patrón de diseño Observer.</p>
 */
public class ListNotifier implements Observer {

    private List<String> log = new ArrayList<>();

    /**
     * Agrega el mensaje recibido al registro interno.
     * @param message mensaje que indica el evento ocurrido.
     */
    @Override
    public void update(String message) {
        log.add(message);
    }

    /**
     * 
     * Regresa la lista completa de mensajes registrados.
     *
     * @return lista de cadenas con los mensajes almacenados.
     */
    public List<String> getLog() {
        return log;

    }
}