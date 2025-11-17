package mx.unam.ciencias.myp.butchery.model.patrones.observer;

import java.io.FileWriter;

import java.io.IOException;

/**
 * Observador encargado de registrar los cambios del sistema en un archivo de texto.
 *
 * <p>Este notifier se utiliza para dejar una evidencia persistente de los eventos relacionados con el inventario u otros elementos del sistema. Cada vez que el sujeto notifica un cambio, el mensaje recibido se escribe al final del archivo configurado.</p>
 *
 * <p>Forma parte de la implementación del patrón de diseño Observer.</p>
 */
public class FileNotifier implements Observer {

    private String filename = "inventory_changes_log.txt";


    /**
     * 
     * Registra el mensaje recibido al final del archivo de texto.
     *
     * @param message mensaje que describe el evento ocurrido.
     */
    @Override

    public void update(String message) {
        
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}