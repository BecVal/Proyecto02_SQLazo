package mx.unam.ciencias.myp.butchery.model.patrones.observer;


/**
 * 
 * Interfaz que define el comportamiento de un sujeto dentro del patrón de diseño Observer.
 *
 * <p>Un sujeto mantiene una colección de observadores y se encarga de notificarles cuando ocurre algún evento relevante. Esto permite desacoplar la lógica que genera los eventos de las acciones que reaccionan a ellos.</p>
 */
public interface Subject {

    /**
     * Registra un observador para que reciba notificaciones del sujeto.
     * @param o observador que será agregado.
     */
    void registerObserver(Observer o);

    /**
     * Elimina un observador de la lista de suscriptores del sujeto.
     * @param o observador que será removido.
     */
    void removeObserver(Observer o);


    /**
     * 
     * Notifica a todos los observadores registrados enviándoles el mensaje indicado.
     * @param message descripción del evento ocurrido.
     */
    void notifyObservers(String message);


}


