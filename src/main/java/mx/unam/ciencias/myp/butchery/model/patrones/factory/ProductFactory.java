package mx.unam.ciencias.myp.butchery.model.patrones.factory;

/**
 * Fábrica para crear objetos de tipo {@link Product}.
 * Encapsula la lógica de instanciación para los diferentes tipos de productos,
 * como {@link ProductByWeight} y {@link ProductByUnit}.
 * Esto permite desacoplar la creación de productos del resto de la aplicación.
 * @author Cesar
 */
public class ProductFactory {

    /**
     * Enumera los tipos de productos que la fábrica puede crear.
     */
    public enum ProductType {
        BY_WEIGHT,
        BY_UNIT
    }

    /**
     * Crea una instancia de un producto proveyendo todos sus atributos.
     *
     * @param id El identificador único del producto.
     * @param type El tipo de producto ({@code BY_WEIGHT} o {@code BY_UNIT}).
     * @param name El nombre del producto.
     * @param unitPrice El precio por unidad (por kg o por pieza).
     * @return Una nueva instancia de {@link Product} correspondiente al tipo especificado.
     * @throws IllegalArgumentException si el {@code type} no es un tipo de producto soportado.
     */
    public Product createProduct(String id, ProductType type, String name, double unitPrice) {
        switch (type) {
            case BY_WEIGHT:
                return new ProductByWeight(id, name, unitPrice);
            case BY_UNIT:
                return new ProductByUnit(id, name, unitPrice);
            default:
                throw new IllegalArgumentException("Unsupported product type: " + type);
        }
    }

    /**
     * Crea una instancia de un producto con valores por defecto.
     * El nombre del producto se establece igual a su ID y el precio se inicializa en 0.0.
     *
     * @param id   El identificador único del producto.
     * @param type El tipo de producto ({@code BY_WEIGHT} o {@code BY_UNIT}).
     * @return Una nueva instancia de {@link Product} con valores por defecto.
     */
    public Product createProduct(String id, ProductType type) {
        return createProduct(id, type, id, 0.0);
    }
}
