package mx.unam.ciencias.myp.butchery.controller;

import mx.unam.ciencias.myp.butchery.model.ModelFacade;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.Product;
import mx.unam.ciencias.myp.butchery.model.domain.Sale;
import mx.unam.ciencias.myp.butchery.model.patrones.strategy.PercentageDiscount;
import mx.unam.ciencias.myp.butchery.model.patrones.strategy.FrequentCustomerDiscount;
import mx.unam.ciencias.myp.butchery.model.patrones.strategy.NoDiscount;
import java.util.List;

/**
 * Controlador principal de la carnicería, que actúa como intermediario
 * entre la vista y el modelo.
 * 
 * @author Luis
 */

public class ButcheryController {
    private ModelFacade model;
    private mx.unam.ciencias.myp.butchery.model.domain.Sale currentSale;

    /**
     * Crea un controlador con el modelo proporcionado.
     * @param model fachada del modelo (no debe ser null)
     */
    public ButcheryController(ModelFacade model) {
        this.model = model;
    }

    /**
     * Inicia una nueva venta en curso que será administrada por el controlador.
     */
    public void beginSale() {
        this.currentSale = new Sale();
    }

    /**
     * Devuelve la venta en curso (puede ser null si no se inició una).
     */
    public Sale getCurrentSale() {
        return this.currentSale;
    }

    /**
     * Agrega un producto por índice a la venta en curso.
     * @param productIndex índice en la lista ordenada (0-based)
     * @param quantity cantidad a agregar
     * @return mensaje de resultado o null si fue exitoso
     */
    public String addProductToCurrentSale(int productIndex, double quantity) {
        if (currentSale == null) return "No active sale.";
        Product p = getProductByIndex(productIndex);
        if (p == null) return "Invalid product selection.";
        double available = model.getStockByProduct(p);
        if (available < quantity) return String.format("Not enough stock. Available: %.2f", available);
        currentSale.addProduct(p, quantity);
        return null;
    }

    /**
     * Obtiene el subtotal (sin descuento) de la venta en curso.
     */
    public double getCurrentSubtotal() {
        if (currentSale == null) return 0.0;
        return currentSale.calculateTotalWithoutDiscount();
    }

    /**
     * Finaliza y procesa la venta en curso aplicando la estrategia correspondiente.
     * Limpia la venta actual cuando termina.
     */
    public String finishCurrentSale(boolean isFrequent, double discountPercent) {
        if (currentSale == null) return "No active sale to finish.";
        String res = performSale(currentSale, isFrequent, discountPercent);
        this.currentSale = null;
        return res;
    }

    /**
     * Cancela la venta en curso y la limpia.
     */
    public void cancelCurrentSale() {
        if (currentSale != null) {
            currentSale.cancelSale();
            currentSale = null;
        }
    }

    /**
     * El controlador le pide al modelo el inventario de productos.
     * @return lista de productos
     */
    public List<Product> getInventoryProducts() {
        return model.getInventory();
    }

    /**
     * El controlador le pide al modelo calcular el precio de un producto
     *
     * @param name nombre del producto
     * @param quantity cantidad (kg o unidades)
     * @param discount descuento a aplicar
     */
    public void calculatePrice(String name, double quantity, double discount) {
        model.calculatePrice(name, quantity, discount);
    }

    /**
     * El controlador le pide al modelo registrar un producto que se vende por unidad.
     *
     * @param id identificador único del producto
     * @param name nombre del producto
     * @param pricePerUnit precio por unidad
     */
    public void registerProductByUnit(String id, String name, double pricePerUnit) {
        model.addProductByUnit(id, name, pricePerUnit);
    }

    /**
     * El controlador le pide al modelo registrar un producto que se vende por peso.
     *
     * @param id identificador único del producto
     * @param name nombre del producto
     * @param pricePerKg precio por kilogramo
     */
    public void registerProductByWeight(String id, String name, double pricePerKg) {
        model.addProductByWeight(id, name, pricePerKg);
    }

    /**
     * Procesa una venta completa compuesta por varios items, aplica la estrategia de descuento
     * según si el cliente es frecuente o el porcentaje provisto, finaliza y procesa la venta.
     *
     * @param sale venta ya construida con items
     * @param isFrequent si es cliente frecuente
     * @param discountPercent porcentaje de descuento (0 si no aplica)
     * @return mensaje con resultado de la operación
     */
    public String performSale(Sale sale, boolean isFrequent, double discountPercent) {
        if (sale == null) return "Sale is null";

        if (isFrequent) {
            sale.setStrategy(new FrequentCustomerDiscount());
        } else if (discountPercent > 0.0) {
            sale.setStrategy(new PercentageDiscount(discountPercent / 100.0));
        } else {
            sale.setStrategy(new NoDiscount());
        }

        sale.applyDiscount();
        sale.finalizeSale();

        try {
            model.processSale(sale);
        } catch (RuntimeException e) {
            return "Error processing sale: " + e.getMessage();
        }

        return String.format("Registered sale. Total: %.2f", sale.getTotal());
    }

    /**
     * El controlador le pide al modelo el historial de ventas.
     * @return lista de ventas
     */
    public java.util.List<Sale> getSalesHistory() {
        return model.getSalesHistory();
    }

    /**
     * El controlador le pide al modelo el total de ingresos por ventas.
     * @return total de ingresos
     */
    public double getTotalRevenue() {
        return model.getTotalRevenue();
    }

    /**
     * El controlador le pide al modelo procesar una venta finalizada.
     * @param sale venta a procesar
     */
    public void processSale(Sale sale) {
        model.processSale(sale);
    }

    /**
     * El controlador le pide al modelo buscar un producto por nombre.
     * 
     * @param name nombre del producto
     * @return producto encontrado o null si no existe
     */
    public Product findProductByName(String name) {
        return model.findProductByName(name);
    }

    /**
     * El controlador le pide al modelo agregar stock a un producto.
     * 
     * @param name nombre del producto
     * @param quantity cantidad a agregar
     */
    public void addStockToProduct(String name, double quantity) {
        model.addStockToProduct(name, quantity);
    }

    /**
     * El controlador le pide al modelo actualizar el nombre de un producto.
     * 
     * @param currentName nombre actual del producto
     * @param newName nuevo nombre del producto
     */
    public void updateProductName(String currentName, String newName) {
        model.updateProductName(currentName, newName);
    }

    /**
     * El controlador le pide al modelo actualizar el precio de un producto.
     * 
     * @param name nombre del producto
     * @param newPrice nuevo precio del producto
     */
    public void updateProductPrice(String name, double newPrice) {
        model.updateProductPrice(name, newPrice);
    }

    /**
     * El controlador le pide al modelo obtener el stock de un producto por nombre.
     * 
     * @param name nombre del producto
     * @return cantidad en stock
     */
    public double getStockByName(String name) {
        return model.getStockByName(name);
    }

    /**
     * El controlador le pide al modelo obtener la lista de productos ordenados por nombre.
     * 
     * @return lista de productos ordenados
     */
    public List<Product> getProductsSorted() {
        return model.getProductsSorted();
    }

    /**
     * El controlador le pide al modelo obtener un producto por su índice en la lista ordenada.
     * 
     * @param index índice del producto
     * @return producto encontrado o null si el índice es inválido
     */
    public Product getProductByIndex(int index) {
        return model.getProductByIndex(index);
    }

    /**
     * El controlador le pide al modelo obtener el stock de un producto.
     * 
     * @param p producto
     * @return cantidad en stock
     */
    public double getStockByProduct(Product p) {
        return model.getStockByProduct(p);
    }

    /**
     * El controlador le pide al modelo eliminar un producto por su índice en la lista ordenada.
     * 
     * @param index índice del producto
     * @return true si se eliminó el producto, false si no se encontró
     */
    public boolean removeProductByIndex(int index) {
        return model.removeProductByIndex(index);
    }

    /**
     * El controlador le pide al modelo sembrar datos de muestra en el inventario.
     */
    public void seedSampleData() {
        model.seedSampleData();
    }
}
