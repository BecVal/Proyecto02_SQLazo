package mx.unam.ciencias.myp.butchery.model.patrones.factory;


/**
 * Representa un producto que se vende por unidad (piezas).
 * Implementa la interfaz {@link Product}.
 *
 * @author Cesar
 */
public class ProductByUnit implements Product {

    private final String id;
    private final String name;
    private final double pricePerUnit;

    /**
     * Construye un nuevo producto que se vende por unidad.
     *
     * @param id           El identificador único del producto.
     * @param name         El nombre del producto.
     * @param pricePerUnit El precio por cada pieza del producto.
     * @throws IllegalArgumentException si el precio por unidad es negativo.
     */
    public ProductByUnit(String id, String name, double pricePerUnit) {
        if (pricePerUnit < 0) throw new IllegalArgumentException("pricePerUnit must be >= 0");
        this.id = id;
        this.name = name;
        this.pricePerUnit = pricePerUnit;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Calcula el precio total para una cantidad específica de piezas.
     * El precio se calcula como {@code precioPorUnidad * cantidad}.
     * Aunque la cantidad es un {@code double}, se espera que represente un número entero de piezas.
     *
     * @param quantity El número de piezas a comprar.
     * @return El precio total calculado.
     * @throws IllegalArgumentException si la cantidad es negativa.
     */
    @Override
    public double calculatePrice(double quantity) {
        if (quantity < 0) throw new IllegalArgumentException("quantity must be >= 0");
        return pricePerUnit * quantity;
    }
    
    /**
     * Obtiene el identificador único del producto.
     *
     * @return El ID del producto.
     */
    public String getId() { return id; }
    
    /**
     * Obtiene el precio por pieza del producto.
     *
     * @return El precio por unidad.
     */
    public double getPricePerUnit() { return pricePerUnit; }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("ProductByUnit{id='%s', name='%s', pricePerUnit=%.2f}", id, name, pricePerUnit);
    }
}
