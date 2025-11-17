package mx.unam.ciencias.myp.butchery.model.patrones.factory;


/**
 * Representa un producto que se vende por peso (kilogramos).
 * Implementa la interfaz {@link Product}.
 *
 * @author Cesar
 */
public class ProductByWeight implements Product {

    private final String id;
    private final String name;
    private final double pricePerKg;

    /**
     * Construye un nuevo producto que se vende por peso.
     *
     * @param id         El identificador único del producto.
     * @param name       El nombre del producto.
     * @param pricePerKg El precio por kilogramo del producto.
     * @throws IllegalArgumentException si el precio por kilogramo es negativo.
     */
    public ProductByWeight(String id, String name, double pricePerKg) {
        if (pricePerKg < 0) throw new IllegalArgumentException("pricePerKg must be >= 0");
        this.id = id;
        this.name = name;
        this.pricePerKg = pricePerKg;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Calcula el precio total para una cantidad específica de peso en kilogramos.
     * El precio se calcula como {@code precioPorKg * cantidad}.
     *
     * @param quantity La cantidad de producto en kilogramos.
     * @return El precio total calculado.
     * @throws IllegalArgumentException si la cantidad es negativa.
     */
    @Override
    public double calculatePrice(double quantity) {
        if (quantity < 0) throw new IllegalArgumentException("quantity must be >= 0");
        return pricePerKg * quantity;
    }
    
    /**
     * Obtiene el identificador único del producto.
     *
     * @return El ID del producto.
     */
    public String getId() { return id; }
    
    /**
     * Obtiene el precio por kilogramo del producto.
     *
     * @return El precio por kilogramo.
     */
    public double getPricePerKg() { return pricePerKg; }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("ProductByWeight{id='%s', name='%s', pricePerKg=%.2f}", id, name, pricePerKg);
    }
}
