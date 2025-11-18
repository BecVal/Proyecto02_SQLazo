package mx.unam.ciencias.myp.butchery.model.patrones.state;

import mx.unam.ciencias.myp.butchery.model.domain.Sale;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.Product;

/**
 * Estado que representa una venta pendiente.
 *
 * <p>En este estado la venta se encuentra activa y es posible modificarla: agregar productos, aplicar descuentos y cambiar su estado a pagada o cancelada.
 * 
 * Este es el único estado donde se permite alterar el contenido de la venta.</p>
 *
 * <p>Forma parte de la implementación del patrón State, que permite controlar el comportamiento de la venta según su estado interno.</p>
 * 
 */
public class PendingState implements ISaleState {
    /**
     * Agrega un producto a la venta.
     * <p>Si el producto ya existe en la venta, la cantidad se incrementa; de lo contrario, se agrega una nueva entrada al mapa de productos.</p>
     * @param sale    la venta que se está modificando.
     * @param product el producto que se desea agregar.
     * @param quantity la cantidad a añadir.
     */
    @Override
    public void addProduct(Sale sale, Product product, double quantity) {

        sale.getItems().put(product, sale.getItems().getOrDefault(product, 0.0) + quantity);
        System.out.println("Producto agregado correctamente.");
    }

    /**
     * Aplica la estrategia de descuento configurada en la venta.
     *
     * <p>Primero se calcula el total sin descuento, luego se delega el cálculo del descuento a la estrategia asignada, y finalmente se actualiza el total.</p>
     * @param sale la venta sobre la cual se aplica el descuento.
     */
    @Override
    public void applyDiscount(Sale sale) {
        double total = sale.calculateTotalWithoutDiscount();
        double newTotal = sale.getStrategy().applyDiscount(total);

        sale.setTotal(newTotal);
        System.out.println("\nDiscount applied.");
    }

    /**
     * Finaliza la venta, cambiando su estado a {@code PaidState}.
     * @param sale la venta que se desea finalizar.
     * 
     */
    @Override
    public void finalizeSale(Sale sale) {

        sale.setState(new PaidState());
        System.out.println("\nSale completed.");

    }

    /**
     * 
     * Cancela la venta, cambiando su estado a {@code CanceledState}.
     * @param sale la venta que se desea cancelar.
     * 
     */
    @Override

    public void cancelSale(Sale sale) {
        sale.setState(new CanceledState());
        System.out.println("\nCanceling...");

    }
}