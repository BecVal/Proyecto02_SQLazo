package mx.unam.ciencias.myp.butchery.model.patrones.factory;

/**
 * Interfaz que representa un producto en la carnicería.
 * @author Cesar
 */
public interface Product {

    /**
     * Obtiene el nombre del producto.
     *
     * @return nombre del producto
     */
    String getName();

    /**
     * Identificador único del producto.
     * @return id
     */
    String getId();

    /**
     * Calcula el precio para una cantidad dada.
     * Para productos por peso la cantidad es en kilogramos,
     * para productos por unidad la cantidad es en piezas.
     *
     * @param quantity cantidad (kg o unidades)
     * @return precio calculado
     * @throws IllegalArgumentException si quantity < 0
     */
    double calculatePrice(double quantity);
}
