package mx.unam.ciencias.myp.butchery.model.patrones.strategy;

/**
 * Estrategia de descuento para clientes frecuentes.
 *
 * <p>Esta estrategia aplica un descuento fijo del 10% sobre el total de la venta. Su propósito es beneficiar a clientes recurrentes sin necesidad de modificar la lógica de cálculo del precio en otras partes del sistema, siguiendo el patrón Strategy.</p>
 * 
 */
public class FrequentCustomerDiscount implements IDiscountStrategy {
    private static final double DISCOUNT = 0.10;

    /**
     * Aplica el descuento correspondiente al total recibido.
     * @param total el monto antes del descuento.
     * 
     * @return el total con el descuento aplicado.
     */
    @Override
    public double applyDiscount(double total) {
        return total - (total * DISCOUNT);

    }
}