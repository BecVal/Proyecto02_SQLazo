package mx.unam.ciencias.myp.butchery.model.patrones.state;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.Product;
import mx.unam.ciencias.myp.butchery.model.domain.Sale;

/**
 * Interfaz que define el comportamiento que deben implementar los distintos estados de una venta dentro del patrón State.
 *
 * <p>Cada estado es responsable de determinar qué operaciones están permitidas y cuáles deben ser rechazadas según la situación actual de la venta (pendiente, pagada o cancelada).</p>
 */
public interface ISaleState {

    /**
     * Intenta agregar un producto a la venta según su estado actual.
     * @param sale    la venta sobre la cual se opera.
     * @param product el producto que se intenta agregar.
     * 
     * @param quantity la cantidad del producto.
     */
    void addProduct(Sale sale, Product product, double quantity);

    /**
     * Intenta aplicar un descuento a la venta según su estado actual.
     * @param sale la venta sobre la cual se opera.
     */
    void applyDiscount(Sale sale);

    
    /**
     * 
     * Intenta finalizar la venta según su estado actual.
     *
     * @param sale la venta que se desea finalizar.
     */
    void finalizeSale(Sale sale);

    /**
     * 
     * Intenta cancelar la venta según su estado actual.
     * @param sale la venta que se desea cancelar.
     */
    void cancelSale(Sale sale);
}

