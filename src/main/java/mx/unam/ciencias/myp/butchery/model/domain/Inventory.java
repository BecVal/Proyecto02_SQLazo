package mx.unam.ciencias.myp.butchery.model.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.unam.ciencias.myp.butchery.model.patrones.observer.Observer;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.*;

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
    public void addStockByUnit(Product product, Double quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }

        stock.put(product, stock.getOrDefault(product, 0.0) + quantity);

        notifyObservers(
            "Added to inventory: " + product.getName() +
            " | Quantity: " + quantity +
            " | Current total: " + stock.get(product)
        );
    }

    /**
     * Agrega una cantidad de un producto vendido por peso al inventario.
     * Si el producto ya existe, actualiza su cantidad. Si no, lo añade.
     *
     * @param product El producto a agregar o actualizar.
     * @param quantity La cantidad a agregar en kilogramos. Debe ser un valor positivo.
     * @throws IllegalArgumentException si la cantidad es menor o igual a cero.
     */
    public void addStockByWeight(Product product, Double quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be > 0");
        }

        stock.put(product, stock.getOrDefault(product, 0.0) + quantity);

        notifyObservers(
            "Added to inventory: " + product.getName() +
            " | Quantity (kg): " + quantity +
            " | Current total (kg): " + stock.get(product)
        );
    }

    /**
     * Agrega un nuevo producto que se vende por unidad al inventario.
     *
     * @param id         El identificador único del producto.
     * @param name       El nombre del producto.
     * @param pricePerUnit El precio por unidad del producto.
     * @throws IllegalArgumentException si ya existe un producto con el mismo nombre.
     */
    public void addProductByUnit(String id, String name, double pricePerUnit) {
        for (Product p : stock.keySet()) {
            if (p.getName().equalsIgnoreCase(name))
                throw new IllegalArgumentException("Product with same name already exists: " + name);
        }
        ProductFactory factory = new ProductFactory();
        Product product = factory.createProduct(id, ProductFactory.ProductType.BY_UNIT, name, pricePerUnit);
        stock.putIfAbsent(product, 0.0);
        notifyObservers("Product added: " + name + " | Price per unit: " + pricePerUnit);
    }

    /**
     * Agrega un nuevo producto que se vende por peso al inventario.
     *
     * @param id         El identificador único del producto.
     * @param name       El nombre del producto.
     * @param pricePerKg El precio por kilogramo del producto.
     * @throws IllegalArgumentException si ya existe un producto con el mismo nombre.
     */
    public void addProductByWeight(String id, String name, double pricePerKg) {
        for (Product p : stock.keySet()) {
            if (p.getName().equalsIgnoreCase(name))
                throw new IllegalArgumentException("Product with same name already exists: " + name);
        }
        ProductFactory factory = new ProductFactory();
        Product product = factory.createProduct(id, ProductFactory.ProductType.BY_WEIGHT, name, pricePerKg);
        stock.putIfAbsent(product, 0.0);
        notifyObservers("Product added: " + name + " | Price per kg: " + pricePerKg);
    }

    /**
     * Obtiene un producto del inventario por su nombre.
     *
     * @param name El nombre del producto a buscar.
     * @return El producto encontrado, o {@code null} si no existe.
     */
    public Product getProductByName(String name) {
        for (Product p : stock.keySet()) {
            if (p.getName().equalsIgnoreCase(name))
                return p;
        }
        return null;
    }

    /**
     * Agrega stock a un producto identificado por su nombre.
     *
     * @param name     El nombre del producto al que se le agregará stock.
     * @param quantity La cantidad a agregar (en kg o unidades). Debe ser un valor positivo.
     * @throws IllegalArgumentException si el producto no se encuentra o si la cantidad es menor o igual a cero.
     */
    public void addStockByProductName(String name, Double quantity) {
        Product p = getProductByName(name);
        if (p == null)
            throw new IllegalArgumentException("Product not found: " + name);
        if (p instanceof ProductByUnit) {
            addStockByUnit(p, quantity);
        } else if (p instanceof ProductByWeight) {
            addStockByWeight(p, quantity);
        } else {
            throw new IllegalStateException("Unknown product type");
        }
    }

    /**
     * Actualiza el nombre de un producto en el inventario.
     *
     * @param currentName El nombre actual del producto.
     * @param newName     El nuevo nombre que se asignará al producto.
     * @throws IllegalArgumentException si no se encuentra el producto con el nombre actual
     *                                  o si ya existe otro producto con el nuevo nombre.
     */
    public void updateProductName(String currentName, String newName) {
        Product existing = getProductByName(currentName);
        if (existing == null)
            throw new IllegalArgumentException("Product not found: " + currentName);
        Product conflict = getProductByName(newName);
        if (conflict != null)
            throw new IllegalArgumentException("Another product already has the name: " + newName);

        double qty = stock.getOrDefault(existing, 0.0);
        stock.remove(existing);

        ProductFactory factory = new ProductFactory();
        Product newProduct;
        if (existing instanceof ProductByUnit) {
            double price = ((ProductByUnit) existing).getPricePerUnit();
            newProduct = factory.createProduct(existing.getId(), ProductFactory.ProductType.BY_UNIT, newName, price);
        } else {
            double price = ((ProductByWeight) existing).getPricePerKg();
            newProduct = factory.createProduct(existing.getId(), ProductFactory.ProductType.BY_WEIGHT, newName, price);
        }
        stock.put(newProduct, qty);
        notifyObservers("Product renamed: " + currentName + " -> " + newName);
    }

    /**
     * Actualiza el precio de un producto en el inventario.
     *
     * @param name     El nombre del producto cuyo precio se actualizará.
     * @param newPrice El nuevo precio que se asignará al producto.
     * @throws IllegalArgumentException si no se encuentra el producto con el nombre dado.
     */
    public void updateProductPrice(String name, double newPrice) {
        Product existing = getProductByName(name);
        if (existing == null)
            throw new IllegalArgumentException("Product not found: " + name);
        double qty = stock.getOrDefault(existing, 0.0);
        stock.remove(existing);

        ProductFactory factory = new ProductFactory();
        Product newProduct;
        if (existing instanceof ProductByUnit) {
            newProduct = factory.createProduct(existing.getId(), ProductFactory.ProductType.BY_UNIT, existing.getName(), newPrice);
        } else {
            newProduct = factory.createProduct(existing.getId(), ProductFactory.ProductType.BY_WEIGHT, existing.getName(), newPrice);
        }
        stock.put(newProduct, qty);
        notifyObservers("Product price updated: " + name + " | New price: " + newPrice);
    }

    /**
     * Elimina un producto del inventario por su nombre.
     *
     * @param name El nombre del producto a eliminar.
     * @return {@code true} si el producto fue eliminado, {@code false} si no se encontró.
     */
    public boolean removeProductByName(String name) {
        Product existing = getProductByName(name);
        if (existing == null)
            return false;
        stock.remove(existing);
        notifyObservers("Product removed: " + name);
        return true;
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
                "There is not enough stock of: " + product.getName()
            );
        }

        stock.put(product, current - quantity);

        notifyObservers(
            "Stock reduced: " + product.getName() +
            " | Amount withdrawn: " + quantity +
            " | Remaining: " + stock.get(product)
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

    public List<Product> getProducts() {
        return new ArrayList<>(stock.keySet());
    }

    /**
     * Devuelve una lista de productos ordenados alfabéticamente por su nombre.
     *
     * @return Una lista ordenada de productos.
     */
    public List<Product> getProductsSortedByName() {
        List<Product> list = getProducts();
        list.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return list;
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
