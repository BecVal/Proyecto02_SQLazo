package mx.unam.ciencias.myp.butchery.model.domain;

import mx.unam.ciencias.myp.butchery.DatabaseManager;
import mx.unam.ciencias.myp.butchery.model.patrones.observer.Observer;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.*;

import java.sql.*;
import java.util.*;

/**
 * Gestiona el inventario de productos utilizando un Singleton.
 * <p>
 * Ahora esta clase incluye persistencia en SQLite:
 * cada operación relevante se sincroniza con la base de datos.
 * </p>
 */
public class Inventory {

    private static Inventory instance;

    private final Map<Product, Double> stock;
    private final List<Observer> observers;

    private final ProductFactory productFactory = new ProductFactory();

    /**
     * Constructor privado.
     * Ahora carga los productos desde SQLite al iniciar.
     */
    private Inventory() {
        this.stock = new HashMap<>();
        this.observers = new ArrayList<>();

        loadFromDatabase();
    }

    /**
     * Devuelve la única instancia de Inventory.
     */
    public static synchronized Inventory getInstance() {
        if (instance == null) {
            instance = new Inventory();
        }
        return instance;
    }

    /* ============================================================
       ===============     PERSISTENCIA (SQLite)     ===============
       ============================================================ */

    /**
     * Carga todos los productos desde la base de datos.
     */
    private void loadFromDatabase() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM inventory")) {

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                double qty = rs.getDouble("stock");
                String typeStr = rs.getString("type");

                ProductFactory.ProductType type =
                        typeStr.equals("BY_UNIT")
                                ? ProductFactory.ProductType.BY_UNIT
                                : ProductFactory.ProductType.BY_WEIGHT;

                Product p = productFactory.createProduct(id, type, name, price);
                stock.put(p, qty);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading inventory from DB", e);
        }
    }

    /**
     * Guarda un producto nuevo en la base de datos.
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
     * Elimina un producto por su ID en la base de datos.
     */
    private void deleteFromDatabase(Product p) {
        String sql = "DELETE FROM inventory WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    private double getPrice(Product p) {
        if (p instanceof ProductByUnit)
            return ((ProductByUnit) p).getPricePerUnit();
        return ((ProductByWeight) p).getPricePerKg();
    }

    /* ============================================================
       ===========  LÓGICA DE INVENTARIO (LA QUE YA TENÍAS)  =======
       ============================================================ */

    public void addStockByUnit(Product product, Double quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");

        stock.put(product, stock.getOrDefault(product, 0.0) + quantity);
        updateInDatabase(product);

        notifyObservers("Added to inventory: " + product.getName() +
                " | Quantity: " + quantity +
                " | Current total: " + stock.get(product));
    }

    public void addStockByWeight(Product product, Double quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");

        stock.put(product, stock.getOrDefault(product, 0.0) + quantity);
        updateInDatabase(product);

        notifyObservers("Added to inventory: " + product.getName() +
                " | Quantity (kg): " + quantity +
                " | Current total (kg): " + stock.get(product));
    }

    public void addProductByUnit(String id, String name, double pricePerUnit) {
        for (Product p : stock.keySet()) {
            if (p.getName().equalsIgnoreCase(name)) {
                updateProductPrice(name, pricePerUnit);
                return;
            }
            
        }

        Product p = productFactory.createProduct(id,
                ProductFactory.ProductType.BY_UNIT, name, pricePerUnit);

        stock.put(p, 0.0);
        insertIntoDatabase(p, 0.0);

        notifyObservers("Product added: " + name + " | Price per unit: " + pricePerUnit);
    }

    public void addProductByWeight(String id, String name, double pricePerKg) {
        for (Product p : stock.keySet()) {
            if (p.getName().equalsIgnoreCase(name)) {
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

    public Product getProductByName(String name) {
        for (Product p : stock.keySet())
            if (p.getName().equalsIgnoreCase(name))
                return p;
        return null;
    }

    public void addStockByProductName(String name, Double quantity) {
        Product p = getProductByName(name);
        if (p == null) throw new IllegalArgumentException("Product not found: " + name);

        if (p instanceof ProductByUnit) addStockByUnit(p, quantity);
        else addStockByWeight(p, quantity);
    }

    public void updateProductName(String currentName, String newName) {
        Product existing = getProductByName(currentName);
        if (existing == null)
            throw new IllegalArgumentException("Product not found: " + currentName);

        if (getProductByName(newName) != null)
            throw new IllegalArgumentException("Name already exists: " + newName);

        double qty = stock.get(existing);
        stock.remove(existing);

        Product newProd;
        if (existing instanceof ProductByUnit) {
            newProd = productFactory.createProduct(existing.getId(),
                    ProductFactory.ProductType.BY_UNIT, newName,
                    ((ProductByUnit) existing).getPricePerUnit());
        } else {
            newProd = productFactory.createProduct(existing.getId(),
                    ProductFactory.ProductType.BY_WEIGHT, newName,
                    ((ProductByWeight) existing).getPricePerKg());
        }

        stock.put(newProd, qty);
        updateInDatabase(newProd);

        notifyObservers("Product renamed: " + currentName + " -> " + newName);
    }

    public void updateProductPrice(String name, double newPrice) {
        Product existing = getProductByName(name);
        if (existing == null)
            throw new IllegalArgumentException("Product not found: " + name);

        double qty = stock.get(existing);
        stock.remove(existing);

        Product newProd;
        if (existing instanceof ProductByUnit) {
            newProd = productFactory.createProduct(existing.getId(),
                    ProductFactory.ProductType.BY_UNIT, name, newPrice);
        } else {
            newProd = productFactory.createProduct(existing.getId(),
                    ProductFactory.ProductType.BY_WEIGHT, name, newPrice);
        }

        stock.put(newProd, qty);
        updateInDatabase(newProd);

        notifyObservers("Product price updated: " + name + " -> " + newPrice);
    }

    public boolean removeProductByName(String name) {
        Product p = getProductByName(name);
        if (p == null) return false;

        stock.remove(p);
        deleteFromDatabase(p);

        notifyObservers("Product removed: " + name);
        return true;
    }

    public void reduceStock(Product product, Double quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");

        double current = stock.getOrDefault(product, 0.0);
        if (current < quantity)
            throw new IllegalStateException("Insufficient stock for: " + product.getName());

        stock.put(product, current - quantity);
        updateInDatabase(product);

        notifyObservers("Stock reduced: " + product.getName() +
                " | Withdrawn: " + quantity +
                " | Remaining: " + stock.get(product));
    }

    public Double getStock(Product product) {
        return stock.getOrDefault(product, 0.0);
    }

    public Map<Product, Double> getInventory() {
        return Collections.unmodifiableMap(stock);
    }

    public List<Product> getProducts() {
        return new ArrayList<>(stock.keySet());
    }

    public List<Product> getProductsSortedByName() {
        List<Product> list = getProducts();
        list.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
        return list;
    }

    public void register(Observer obs) {
        observers.add(obs);
    }

    public void notifyObservers(String msg) {
        for (Observer o : observers) o.update(msg);
    }
}
