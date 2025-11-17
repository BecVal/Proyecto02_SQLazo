package mx.unam.ciencias.myp.butchery.model.patrones.state;
import mx.unam.ciencias.myp.butchery.model.domain.Sale;

import mx.unam.ciencias.myp.butchery.model.patrones.factory.Product;

/**
 * Estado que representa una venta que ya ha sido pagada.
 *
 * <p>En este estado no es posible realizar ninguna modificación a la venta: no se pueden agregar productos, aplicar descuentos, finalizar nuevamente ni cancelar la operación. Cualquier intento de realizar alguna de estas acciones será rechazado.</p>
 * <p>Este comportamiento forma parte del patrón State, que permite definir dinámicamente las operaciones válidas según el estado actual de la venta.</p>
 */
public class PaidState implements ISaleState {

    /**
     * Muestra un mensaje indicando que la operación no está permitida debido a que la venta ya ha sido pagada.
     */
    private void reject() {

        System.out.println("Operación no permitida: la venta ya está pagada.");
    }

    /**
     * Intento de agregar un producto a una venta ya pagada.
     * Siempre es rechazado.
     * 
     */
    @Override
    public void addProduct(Sale sale, Product product, double quantity) {
        reject();
    }

    /**
     * Intento de aplicar un descuento a una venta ya pagada.
     * 
     * Siempre es rechazado.
     */
    @Override
    public void applyDiscount(Sale sale) {
        reject();
    }

    /**
     * Intento de finalizar una venta que ya fue finalizada y pagada.
     * 
     * Siempre es rechazado.
     */

    @Override

    public void finalizeSale(Sale sale) {
        reject();
    }
    /**
     * Intento de cancelar una venta ya pagada.
     * Siempre es rechazado.
     */
    @Override
    public void cancelSale(Sale sale) {
        reject();
    }

}