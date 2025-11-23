package mx.unam.ciencias.myp.butchery.model.domain;

import mx.unam.ciencias.myp.butchery.DatabaseManager;
import mx.unam.ciencias.myp.butchery.model.patrones.observer.Observer;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.*;

import java.sql.*;
import java.util.*;

/**
 * Gestiona el inventario de productos de la carnicería utilizando el patrón Singleton
 * para garantizar una única instancia global.
 *
 * <p>
 * Esta clase actúa también como el sujeto del patrón Observer, notificando a los observadores
 * cuando ocurre cualquier modificación en el estado del inventario (agregar productos, modificar,
 * eliminar, cambiar stock).
 * </p>
 *
 * <p>
 * Además, esta versión incorpora persistencia real mediante SQLite.
 * Al inicializarse, el inventario se carga desde la base de datos,
 * y cada operación relevante actualiza automáticamente la tabla correspondiente.
 * </p>
 */
public class Inventory {

    private static Inventory instance;
    private final Map<Product, Double> stock;

    private final List<Observer> observers;

    private final ProductFactory productFactory = new ProductFactory();

    /**
     * Constructor privado para la implementación del patrón Singleton.
     *
     * <p>
     * En esta versión, además de inicializar las estructuras internas, se realiza la carga
     * completa del inventario desde la base de datos SQLite.
     * </p>
     */
    private Inventory() {
        this.stock = new HashMap<>();
        this.observers = new ArrayList<>();

        loadFromDatabase();
    }

    /**
     * Devuelve la única instancia de Inventory.
     * Si no existe, la crea.
     *
     * @return la instancia única del inventario
     */
    public static synchronized Inventory getInstance() {
        if (instance == null) {
            instance = new Inventory();
        }
        return instance;
    }

    /**
     * Carga todos los productos desde la base de datos en memoria.
     *
     * <p>
     * Este método se llama automáticamente al construir el inventario y garantiza que
     * el estado persistente se vea reflejado en la aplicación.
     * </p>
     */
    private void loadFromDatabase() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM inventory")) {

            while (rs.next()) {
                String id   = rs.getString("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                double qty   = rs.getDouble("stock");
                String typeStr = rs.getString("type");

                ProductFactory.ProductType type = typeStr.equals("BY_UNIT") ? ProductFactory.ProductType.BY_UNIT : ProductFactory.ProductType.BY_WEIGHT;
                Product p = productFactory.createProduct(id, type, name, price);
                stock.put(p, qty);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error loading inventory from DB", e);
        }
    }

    /**
     * Inserta un nuevo producto en la base de datos.
     *
     * @param p   el producto nuevo
     * @param qty la cantidad inicial (normalmente 0)
     */
    private void insertIntoDatabase(Product p, double qty) {
        String sql = "INSERT INTO inventory(id,name,price,stock,type) VALUES (?,?,?,?,?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getId());
            ps.setString(2, p.getName());
            ps.setDouble(3, getPrice(p));
            ps.setDouble(4, qty);
            ps.setString(5, p instanceof ProductByUnit ? "BY_UNIT" : "BY_WEIGHT");

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Error inserting product", e);
        }
    }

    /**
     * Actualiza un producto existente en la base de datos.
     *
     * @param p el producto cuya información debe persistirse
     */
    private void updateInDatabase(Product p) {
        String sql = "UPDATE inventory SET name=?, price=?, stock=? WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setDouble(2, getPrice(p));
            ps.setDouble(3, stock.get(p));
            ps.setString(4, p.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    /**
     * Elimina el producto correspondiente en la base de datos.
     *
     * @param p el producto a eliminar
     */
    private void deleteFromDatabase(Product p) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM inventory WHERE id=?")) {

            ps.setString(1, p.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    /**
     * Obtiene el precio de un producto sin importar su tipo.
     *
     * @param p producto
     * @return precio aplicado a su tipo correspondiente
     */
    private double getPrice(Product p) {
        if (p instanceof ProductByUnit)
            return ((ProductByUnit) p).getPricePerUnit();
        return ((ProductByWeight) p).getPricePerKg();
    }

    /**
     * Agrega stock a un producto vendido por unidad.
     *
     * <p>Actualiza tanto la estructura interna como la base de datos SQLite.</p>
     */
    public void addStockByUnit(Product product, Double quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");

        stock.put(product, stock.getOrDefault(product, 0.0) + quantity);
        updateInDatabase(product);

        notifyObservers("Added to inventory: " + product.getName() + " | Quantity: " + quantity + " | Current total: " + stock.get(product));
    }

    /**
     * Agrega stock a un producto vendido por peso.
     *
     * <p>Actualiza tanto la estructura interna como la base de datos SQLite.</p>
     */
    public void addStockByWeight(Product product, Double quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");
        stock.put(product, stock.getOrDefault(product, 0.0) + quantity);
        updateInDatabase(product);

        notifyObservers("Added to inventory: " + product.getName() + " | Quantity (kg): " + quantity + " | Current total (kg): " + stock.get(product));
    }

    /**
     * Agrega un nuevo producto vendido por unidad al inventario.
     *
     * <p>
     * Si el nombre ya existe, en lugar de detener el flujo,
     * se interpreta como una actualización de precio, de acuerdo a tu implementación.
     * </p>
     */
    public void addProductByUnit(String id, String name, double pricePerUnit) {

        for (Product existing : stock.keySet()) {
            if (existing.getName().equalsIgnoreCase(name)) {
                updateProductPrice(name, pricePerUnit);
                return;
            }
        }

        Product p = productFactory.createProduct(id, ProductFactory.ProductType.BY_UNIT, name, pricePerUnit);
        stock.put(p, 0.0);
        insertIntoDatabase(p, 0.0);

        notifyObservers("Product added: " + name + " | Price per unit: " + pricePerUnit);
    }

    /**
     * Agrega un nuevo producto vendido por peso al inventario.
     *
     * <p>
     * Si ya existe un producto con el mismo nombre, se actualiza su precio en lugar de lanzar una excepción.
     * </p>
     */
    public void addProductByWeight(String id, String name, double pricePerKg) {

        for (Product existing : stock.keySet()) {
            if (existing.getName().equalsIgnoreCase(name)) {
                updateProductPrice(name, pricePerKg);
                return;
            }
        }

        Product p = productFactory.createProduct(id,
                ProductFactory.ProductType.BY_WEIGHT, name, pricePerKg);

        stock.put(p, 0.0);
        insertIntoDatabase(p, 0.0);

        notifyObservers("Product added: " + name + " | Price per kg: " + pricePerKg);
    }

    /**
     * Obtiene un producto por nombre, o {@code null} si no existe.
     */
    public Product getProductByName(String name) {
        for (Product p : stock.keySet())
            if (p.getName().equalsIgnoreCase(name)) return p;
        return null;
    }

    /**
     * Agrega stock según el tipo de producto (unidad o peso).
     */
    public void addStockByProductName(String name, Double quantity) {
        Product p = getProductByName(name);
        if (p == null) throw new IllegalArgumentException("Product not found: " + name);
        if (p instanceof ProductByUnit) addStockByUnit(p, quantity);
        else addStockByWeight(p, quantity);
    }

    /**
     * Actualiza el nombre de un producto en inventario y en la base de datos.
     *
     * @throws IllegalArgumentException si el producto no existe o si el nuevo nombre ya está en uso
     */
    public void updateProductName(String currentName, String newName) {
        Product existing = getProductByName(currentName);
        if (existing == null) throw new IllegalArgumentException("Product not found: " + currentName);

        if (getProductByName(newName) != null)
            throw new IllegalArgumentException("Name already exists: " + newName);

        double qty = stock.get(existing);
        stock.remove(existing);

        Product newProd;
        if (existing instanceof ProductByUnit) {
            newProd = productFactory.createProduct(existing.getId(), ProductFactory.ProductType.BY_UNIT, newName, ((ProductByUnit) existing).getPricePerUnit());
        } else {
            newProd = productFactory.createProduct(existing.getId(), ProductFactory.ProductType.BY_WEIGHT, newName, ((ProductByWeight) existing).getPricePerKg());
        }

        stock.put(newProd, qty);
        updateInDatabase(newProd);

        notifyObservers("Product renamed: " + currentName + " -> " + newName);

    }

    /**
     * Actualiza el precio de un producto existente.
     */

    public void updateProductPrice(String name, double newPrice) {
        Product existing = getProductByName(name);
        if (existing == null)
            throw new IllegalArgumentException("Product not found: " + name);

        double qty = stock.get(existing);
        stock.remove(existing);

        Product newProd;
        if (existing instanceof ProductByUnit) {
            newProd = productFactory.createProduct(existing.getId(), ProductFactory.ProductType.BY_UNIT, name, newPrice);
        } else {

            newProd = productFactory.createProduct(existing.getId(), ProductFactory.ProductType.BY_WEIGHT, name, newPrice);
        }

        stock.put(newProd, qty);

        updateInDatabase(newProd);

        notifyObservers("Product price updated: " + name + " -> " + newPrice);
    }

    /**
     * Elimina un producto por nombre tanto en memoria como en SQLite.
     * @return true si el producto existía y fue eliminado; false en caso contrario
     */
    public boolean removeProductByName(String name) {
        Product p = getProductByName(name);
        if (p == null) return false;

        stock.remove(p);
        deleteFromDatabase(p);

        notifyObservers("Product removed: " + name);
        return true;
    }

    /**
     * 
     * Reduce del inventario la cantidad indicada de un producto.
     * <p>
     * Este método valida que exista suficiente stock antes de descontar.
     * </p>
     */
    public void reduceStock(Product product, Double quantity) {
        if (quantity <= 0)throw new IllegalArgumentException("Quantity must be > 0");

        double current = stock.getOrDefault(product, 0.0);
        if (current < quantity)
            throw new IllegalStateException("Insufficient stock for: " + product.getName());

        stock.put(product, current - quantity);
        updateInDatabase(product);

        notifyObservers(
                "Stock reduced: " + product.getName() +
                " | Withdrawn: " + quantity +
                " | Remaining: " + stock.get(product)
        );
    }

    /**
     * Devuelve el stock disponible de un producto.
     *
     * @return cantidad existente en inventario (o 0 si no está)
     */
    public Double getStock(Product product) {
        return stock.getOrDefault(product, 0.0);
    }

    /**
     * Devuelve una vista no modificable del mapa completo del inventario.
     */
    public Map<Product, Double> getInventory() {
        return Collections.unmodifiableMap(stock);
    }

    /**
     * Devuelve una lista de todos los productos registrados.
     */
    public List<Product> getProducts() {
        return new ArrayList<>(stock.keySet());
    }

    /**
     * Devuelve los productos ordenados alfabéticamente por su nombre.
     */
    public List<Product> getProductsSortedByName() {
        List<Product> list = getProducts();
        list.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
        return list;
    }

    /**
     * Registra un observador para recibir notificaciones de cambios.
     */
    public void register(Observer obs) {
        observers.add(obs);
    }

    /**
     * Notifica a todos los observadores registrados con un mensaje.
     */
    public void notifyObservers(String msg) {
        for (Observer o : observers)
            o.update(msg);
    }
}
