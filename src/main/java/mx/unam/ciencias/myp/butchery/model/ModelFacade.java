package mx.unam.ciencias.myp.butchery.model;

import mx.unam.ciencias.myp.butchery.model.domain.Inventory;
import mx.unam.ciencias.myp.butchery.model.domain.Sale;
import mx.unam.ciencias.myp.butchery.model.domain.SalesHistory;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.*;
import mx.unam.ciencias.myp.butchery.model.patrones.strategy.IDiscountStrategy;
import mx.unam.ciencias.myp.butchery.model.patrones.strategy.PercentageDiscount;
import mx.unam.ciencias.myp.butchery.model.patrones.strategy.NoDiscount;
import java.util.List;
import java.util.Map;

/**
 * Fachada que expone las funcionalidades del modelo de la carnicería.
 * Actúa como intermediario entre el controlador y las clases del dominio.
 * 
 * @author Luis
 */
public class ModelFacade {
    private Inventory inventory;
    private SalesHistory salesHistory;

    /**
     * Crea una nueva instancia de la fachada del modelo.
     */
    public ModelFacade() {
        inventory = Inventory.getInstance();
        salesHistory = new SalesHistory();
    }

    /**
     * Obtiene la lista de productos en el inventario.
     * @return lista de productos
     */
    public List<Product> getInventory() {
        return inventory.getProducts();
    }

    /**
     * Busca un producto en el inventario por su nombre.
     *
     * @param name El nombre del producto a buscar.
     * @return El producto encontrado, o {@code null} si no existe.
     */
    public Product findProductByName(String name) {
        return inventory.getProductByName(name);
    }

    /**
     * Agrega stock a un producto identificado por su nombre.
     *
     * @param name     El nombre del producto al que se le agregará stock.
     * @param quantity La cantidad a agregar (en kg o unidades). Debe ser un valor positivo.
     * @throws IllegalArgumentException si el producto no se encuentra o si la cantidad es menor o igual a cero.
     */
    public void addStockToProduct(String name, double quantity) {
        inventory.addStockByProductName(name, quantity);
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
        inventory.updateProductName(currentName, newName);
    }

    /**
     * Actualiza el precio de un producto en el inventario.
     *
     * @param name     El nombre del producto cuyo precio se actualizará.
     * @param newPrice El nuevo precio que se asignará al producto.
     * @throws IllegalArgumentException si no se encuentra el producto con el nombre dado.
     */
    public void updateProductPrice(String name, double newPrice) {
        inventory.updateProductPrice(name, newPrice);
    }

    /**
     * Obtiene el stock de un producto por su nombre.
     *
     * @param name El nombre del producto.
     * @return La cantidad en stock del producto. Si el producto no existe, retorna 0.0.
     */
    public double getStockByName(String name) {
        Product p = findProductByName(name);
        if (p == null) return 0.0;
        return inventory.getStock(p);
    }

    /**
     * Obtiene la lista de productos ordenados alfabéticamente por su nombre.
     *
     * @return Una lista ordenada de productos.
     */
    public java.util.List<Product> getProductsSorted() {
        return inventory.getProductsSortedByName();
    }

    /**
     * Obtiene un producto por su índice en la lista ordenada alfabéticamente.
     *
     * @param index El índice del producto en la lista ordenada.
     * @return El producto en el índice dado, o {@code null} si el índice es inválido.
     */
    public Product getProductByIndex(int index) {
        java.util.List<Product> list = getProductsSorted();
        if (index < 0 || index >= list.size()) return null;
        return list.get(index);
    }

    /**
     * Obtiene el stock de un producto dado.
     *
     * @param p El producto cuyo stock se desea obtener.
     * @return La cantidad en stock del producto.
     */
    public double getStockByProduct(Product p) {
        return inventory.getStock(p);
    }

    /**
     * Elimina un producto del inventario por su índice en la lista ordenada.
     *
     * @param index El índice del producto a eliminar.
     * @return {@code true} si el producto fue eliminado, {@code false} si el índice es inválido.
     */
    public boolean removeProductByIndex(int index) {
        Product p = getProductByIndex(index);
        if (p == null) return false;
        return inventory.removeProductByName(p.getName());
    }

    /**
     * Si no existen, siembra datos de muestra en el inventario.
     */
    public void seedSampleData() {

        try {

            String id1 = java.util.UUID.randomUUID().toString();
            inventory.addProductByWeight(id1, "Carne de res - Bistec", 150.0);
            inventory.addStockByProductName("Carne de res - Bistec", 20.0);

            String id2 = java.util.UUID.randomUUID().toString();
            inventory.addProductByWeight(id2, "Cerdo - Lomo", 130.0);
            inventory.addStockByProductName("Cerdo - Lomo", 10.0);

            String id3 = java.util.UUID.randomUUID().toString();
            inventory.addProductByUnit(id3, "Pollo entero", 80.0);
            inventory.addStockByProductName("Pollo entero", 30.0);

            String id4 = java.util.UUID.randomUUID().toString();
            inventory.addProductByUnit(id4, "Chorizo", 40.0);
            inventory.addStockByProductName("Chorizo", 50.0);

        } catch (IllegalArgumentException e) {
            // Si ya existen, ignorar para permitir múltiples ejecuciones
        }
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
        inventory.addProductByUnit(id, name, pricePerUnit);
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
        inventory.addProductByWeight(id, name, pricePerKg);
    }

    /**
     * Registra una venta en el historial de ventas.
     *
     * @param sale La venta a registrar.
     */
    public void recordSale(Sale sale) {
        salesHistory.addSale(sale);
    }

    /**
     * Obtiene el historial de ventas.
     *
     * @return Lista de ventas realizadas.
     */
    public java.util.List<Sale> getSalesHistory() {
        return salesHistory.getSales();
    }

    /**
     * Obtiene el ingreso total generado por todas las ventas.
     *
     * @return Ingreso total.
     */
    public double getTotalRevenue() {
        return salesHistory.getTotalRevenue();
    }

    /**
     * Procesa una venta ya finalizada: valida stock, reduce cantidades y registra en historial.
     * @param sale venta cuyo estado debe ser Paid (finalizada)
     * @throws IllegalStateException si no hay stock suficiente o la venta no está finalizada
     */
    public void processSale(Sale sale) {
        if (sale == null) throw new IllegalArgumentException("sale cannot be null");

        String stateName = sale.getState().getClass().getSimpleName();
        if (!"PaidState".equals(stateName)) {
            throw new IllegalStateException("Sale must be finalized before processing.");
        }

        for (Map.Entry<Product, Double> e : sale.getItems().entrySet()) {
            Product p = e.getKey();
            double qty = e.getValue();
            double available = inventory.getStock(p);
            if (available < qty) {
                throw new IllegalStateException("Insufficient stock for product: " + p.getName());
            }
        }

        for (Map.Entry<Product, Double> e : sale.getItems().entrySet()) {
            inventory.reduceStock(e.getKey(), e.getValue());
        }

        salesHistory.addSale(sale);
    }

    /**
     * Calcula el precio de un producto dado su nombre, cantidad y descuento.
     *
     * @param name     El nombre del producto.
     * @param quantity La cantidad (en kg o unidades).
     * @param discount El porcentaje de descuento a aplicar.
     */
    public void calculatePrice(String name, double quantity, double discount) {
        inventory.getProducts().stream()
            .filter(product -> product.getName().equalsIgnoreCase(name))
            .findFirst()
            .ifPresent(product -> {
                double price = product.calculatePrice(quantity);
                IDiscountStrategy strategy;
                if (discount > 0.0) {
                    strategy = new PercentageDiscount(discount / 100.0);
                } else {
                    strategy = new NoDiscount();
                }

                double discountedPrice = strategy.applyDiscount(price);
                System.out.println("Original price: " + price);
                System.out.println("Discount price applied: " + discountedPrice);
            });
    }
}