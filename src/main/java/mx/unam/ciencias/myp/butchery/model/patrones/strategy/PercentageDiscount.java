package mx.unam.ciencias.myp.butchery.model.patrones.strategy;
/**
 * Estrategia de descuento porcentual configurable.
 *
 * <p>Esta estrategia permite aplicar un descuento basado en un porcentaje proporcionado al momento de crear la instancia. Es útil para descuentos promocionales, descuentos temporales o cualquier caso donde el porcentaje no sea fijo.</p>
 * <p>Forma parte del patrón Strategy, permitiendo intercambiar dinámicamente el tipo de descuento sin modificar la estructura de la venta.</p>
 * 
 */
public class PercentageDiscount implements IDiscountStrategy {

    private double percent;


    /**
     * Crea una nueva estrategia de descuento porcentual.
     * @param percent el porcentaje de descuento a aplicar expresado como decimal.
     */
    public PercentageDiscount(double percent) {
        this.percent = percent;
    }
    /**
     * Aplica el descuento porcentual al total recibido.
     * 
     * @param total monto original antes de aplicar el descuento.
     * 
     * @return el total con el descuento aplicado.
     */
    @Override
    public double applyDiscount(double total) {

        return total - (total * percent);
    }
    
}