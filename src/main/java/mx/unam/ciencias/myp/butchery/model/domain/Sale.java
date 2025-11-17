package mx.unam.ciencias.myp.butchery.model.domain;
import mx.unam.ciencias.myp.butchery.model.patrones.state.ISaleState;

import mx.unam.ciencias.myp.butchery.model.patrones.state.PendingState;
import mx.unam.ciencias.myp.butchery.model.patrones.strategy.IDiscountStrategy;
import mx.unam.ciencias.myp.butchery.model.patrones.strategy.NoDiscount;

import mx.unam.ciencias.myp.butchery.model.patrones.factory.Product;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa una venta realizada en la carnicería.
 * 
 * <p>Una venta mantiene una colección de productos con sus cantidades, un total acumulado, una estrategia de descuento configurable y un estado interno que determina las operaciones permitidas.</p>
 *
 * <p>Los patrones utilizados en esta clase son:</p>
 * <ul>
 *     <li><b>State:</b> controla las operaciones válidas según el estado de la venta (pendiente, pagada o cancelada).</li>
 *     <li><b>Strategy:</b> permite aplicar distintos tipos de descuento sin afectar la lógica interna.</li>
 * </ul>
 *
 */

public class Sale {

    private Map<Product, Double> items;
    private double total;
    private IDiscountStrategy strategy;
    private ISaleState state;

    /**
     * Crea una nueva venta con estado pendiente, sin descuento y con un mapa vacío de productos.
     */
    public Sale() {
        this.items = new HashMap<>();
        this.total = 0.0;
        this.strategy = new NoDiscount();
        this.state = new PendingState();
    }

    /**
     * Regresa el mapa de productos y cantidades de la venta.
     * @return mapa de productos asociados con su cantidad.
     */
    public Map<Product, Double> getItems() {
        return items;
    }

    /**
     * Regresa el total actual de la venta.
     *
     * @return total acumulado de la venta.
     * 
     */
    public double getTotal() {
        return total;
    }

    /**
     * Establece el total actual de la venta.
     * @param total nuevo valor total.
     */
    public void setTotal(double total) {
        this.total = total;
    }


    /**
     * 
     * 
     * Obtiene la estrategia de descuento utilizada en esta venta.
     *
     * @return estrategia de descuento aplicada.
     */
    public IDiscountStrategy getStrategy() {

        return strategy;
    }
    /**
     * 
     * Cambia la estrategia de descuento de la venta.
     * @param strategy nueva estrategia de descuento.
     */
    public void setStrategy(IDiscountStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Regresa el estado actual de la venta.
     *
     * @return estado de la venta.
     */
    public ISaleState getState() {
        return state;

    }

    /**
     * Cambia el estado interno de la venta.
     *
     * @param state nuevo estado de la venta.
     */
    public void setState(ISaleState state) {

        this.state = state;
    }

    /**
     * Intenta agregar un producto a la venta.
     * 
     * <p>La operación se delega al estado actual de la venta, el cual decide si es válida dependiendo de su situación interna.</p>
     *
     * @param product producto a agregar.
     * 
     * @param quantity cantidad asociada al producto.
     */
    public void addProduct(Product product, double quantity) {
        state.addProduct(this, product, quantity);
    }

    /**
     * Intenta aplicar un descuento a la venta.
     * 
     * <p>La validez de esta acción depende del estado interno de la venta.</p>
     * 
     */
    public void applyDiscount() {
        state.applyDiscount(this);
    }

    /**
     * Intenta finalizar la venta.
     * <p>El cambio a estado pagado solo es válido si la venta está pendiente.</p>
     */
    public void finalizeSale() {
        state.finalizeSale(this);
    }

    /**
     * Intenta cancelar la venta.
     * <p>La validez de esta operación depende del estado actual.</p>
     */

    public void cancelSale() {
        state.cancelSale(this);
    
    }



    /**
     * Calcula el total sin aplicar ningún tipo de descuento.
     * 
     * <p>Este método es utilizado por los estados para recalcular el monto base antes de aplicar la estrategia de descuento.</p>
     *
     * @return total bruto sin descuentos.
     */
    public double calculateTotalWithoutDiscount() {
        double sum = 0.0;
        for (Map.Entry<Product, Double> entry : items.entrySet()) {

            Product product = entry.getKey();

            double quantity = entry.getValue();
            sum += product.calculatePrice(quantity);
        }
        return sum;
    }

}