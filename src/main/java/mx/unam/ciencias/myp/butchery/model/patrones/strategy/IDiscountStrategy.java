package mx.unam.ciencias.myp.butchery.model.patrones.strategy;
/**
 * Interfaz que define el comportamiento de una estrategia de descuento.
 *
 * <p>Forma parte del patrón de diseño Strategy, permitiendo intercambiar dinámicamente distintas formas de calcular descuentos sin modificar la lógica interna de la venta.</p>
 */

public interface IDiscountStrategy {
    /**
     * 
     * Aplica un descuento al total recibido.
     * @param total monto original antes del descuento.
     * @return el total resultante después de aplicar el descuento.
     */
    double applyDiscount(double total);

}

