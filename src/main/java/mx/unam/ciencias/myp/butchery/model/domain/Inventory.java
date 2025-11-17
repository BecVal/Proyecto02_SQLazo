package mx.unam.ciencias.myp.butchery.model.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.unam.ciencias.myp.butchery.model.patrones.observer.Observer;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.Product;

/**
 * Gestiona el inventario de productos de la carnicería utilizando el patrón Singleton
 * para garantizar una única instancia global.
 * <p>
 * Esta clase también actúa como el "sujeto" (o "subject") en el patrón Observer,
 * notificando a los observadores registrados cada vez que el stock de productos
 * es modificado.
 *
 * @author Cesar
 */
public class Inventory {

    private static Inventory instance;
    private final Map<Product, Double> stock;
    private final List<Observer> observers;

    /**
     * Constructor privado para forzar el uso de {@link #getInstance()} y así
     * garantizar la implementación del patrón Singleton.
     * Inicializa las estructuras de datos para el stock y los observadores.
     */
    private Inventory() {
        this.stock = new HashMap<>();
        this.observers = new ArrayList<>();
    }

    /**
     * Devuelve la única instancia de la clase Inventory.
     * Si la instancia no existe, la crea (inicialización perezosa o "lazy initialization").
     * Este método está sincronizado para garantizar la seguridad en entornos de hilos múltiples.
     *
     * @return La única instancia de {@code Inventory}.
     */
    public static synchronized Inventory getInstance() {
        if (instance == null) {
            instance = new Inventory();
        }
        return instance;
    }

    /**
     * Agrega una cantidad de un producto al inventario.
     * Si el producto ya existe, actualiza su cantidad. Si no, lo añade.
     *
     * @param product El producto a agregar o actualizar.
     * @param quantity La cantidad a agregar (en kg o unidades). Debe ser un valor positivo.
     * @throws IllegalArgumentException si la cantidad es menor o igual a cero.
     */
    public void addStock(Product product, Double quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }

        stock.put(product, stock.getOrDefault(product, 0.0) + quantity);

        notifyObservers(
            "Se agregó al inventario: " + product.getName() +
            " | Cantidad: " + quantity +
            " | Total actual: " + stock.get(product)
        );
    }

    /**
     * Reduce la cantidad de un producto en el inventario.
     *
     * @param product El producto del cual se reducirá el stock.
     * @param quantity La cantidad a retirar (en kg o unidades). Debe ser un valor positivo.
     * @throws IllegalArgumentException si la cantidad es menor o igual a cero.
     * @throws IllegalStateException si no hay suficiente stock del producto para retirar la cantidad solicitada.
     */
    public void reduceStock(Product product, Double quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }

        double current = stock.getOrDefault(product, 0.0);

        if (current < quantity) {
            throw new IllegalStateException(
                "No hay suficiente stock de " + product.getName()
            );
        }

        stock.put(product, current - quantity);

        notifyObservers(
            "Se redujo stock: " + product.getName() +
            " | Cantidad retirada: " + quantity +
            " | Restante: " + stock.get(product)
        );
    }

    /**
     * Obtiene la cantidad disponible de un producto.
     *
     * @param product El producto a consultar.
     * @return La cantidad de stock disponible. Devuelve 0.0 si el producto no está en el inventario.
     */
    public Double getStock(Product product) {
        return stock.getOrDefault(product, 0.0);
    }

    /**
     * Devuelve una vista no modificable del mapa completo del inventario.
     * Esto previene modificaciones externas directas al estado del inventario.
     *
     * @return Un mapa no modificable de {@link Product} y su stock {@link Double}.
     */
    public Map<Product, Double> getInventory() {
        return Collections.unmodifiableMap(stock);
    }

    /**
     * Registra un observador para recibir actualizaciones.
     *
     * @param obs El observador a registrar.
     */
    public void register(Observer obs) {
        observers.add(obs);
    }

    /**
     * Notifica a todos los observadores registrados sobre un cambio en el inventario,
     * enviándoles un mensaje descriptivo.
     *
     * @param msg El mensaje que describe el cambio ocurrido.
     */
    public void notifyObservers(String msg) {
        for (Observer obs : observers) {
            obs.update(msg);
        }
    }
}
