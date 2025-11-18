package mx.unam.ciencias.myp.butchery.model.patrones.state;

import mx.unam.ciencias.myp.butchery.model.domain.Sale;

import mx.unam.ciencias.myp.butchery.model.patrones.factory.Product;

/**
 * Estado que representa una venta cancelada.
 *
 * <p>Una vez que la venta entra en este estado, no es posible realizar ninguna operación adicional sobre ella. Todas las acciones válidas en otros estados se rechazan aquí, ya que la venta está completamente inhabilitada.</p>
 * <p>Este estado forma parte de la implementación del patrón State para controlar el comportamiento dinámico de una venta según su situación actual.</p>
 */

public class CanceledState implements ISaleState {
    /**
     * Muestra un mensaje indicando que la operación no está permitida debido a que la venta se encuentra cancelada.
     */
    private void reject() {
        System.out.println("\nIt is not possible.");
    }

    /**
     * Intento de agregar un producto a una venta cancelada.
     * Siempre es rechazado.
     */
    @Override
    public void addProduct(Sale sale, Product product, double quantity) {
        reject();
    }

    /**
     * Intento de aplicar un descuento a una venta cancelada.
     * Siempre es rechazado.
     */
    @Override
    public void applyDiscount(Sale sale) {
        reject();
    }

    /**
     * Intento de finalizar una venta cancelada.
     * Siempre es rechazado.
     */
    @Override
    public void finalizeSale(Sale sale) {

        reject();
    }

    /**
     * Intento de cancelar nuevamente una venta ya cancelada.
     * También es rechazado.
     */
    @Override
    
    public void cancelSale(Sale sale) {
        reject();
    }
}
