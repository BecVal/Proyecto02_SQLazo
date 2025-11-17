package mx.unam.ciencias.myp.butchery.model.patrones.strategy;

/**
 * Estrategia de descuento que no aplica ningún cambio al total.
 * <p>Se utiliza cuando la venta no tiene un descuento asociado.
 * Esta clase forma parte del patrón Strategy, permitiendo que la lógica de cálculo del precio permanezca desacoplada del tipo de descuento utilizado.</p>
 */
public class NoDiscount implements IDiscountStrategy {

    
    /**
     * Regresa el total sin realizar ninguna modificación.
     * @param total monto original antes de aplicar descuento.
     * @return el mismo total recibido, sin alteraciones.
     * 
     */
    @Override
    public double applyDiscount(double total) {
        return total;
    }

}