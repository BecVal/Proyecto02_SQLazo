package mx.unam.ciencias.myp.butchery.test.model.patrones.factory;

import mx.unam.ciencias.myp.butchery.model.patrones.factory.ProductByWeight;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link ProductByWeight}.
 * <p>
 * Estas pruebas validan:
 * <ul>
 *     <li>La correcta inicialización de atributos mediante el constructor.</li>
 *     <li>La validación de precios negativos.</li>
 *     <li>El cálculo del costo según el peso solicitado.</li>
 *     <li>La implementación adecuada de {@code equals()} y {@code hashCode()} basada exclusivamente en el identificador del producto.</li>
 * </ul>
 * 
 * </p>
 */
public class ProductByWeightTest {

    /**
     * 
     * Verifica que el constructor almacene correctamente el ID, nombre y precio por kilogramo del producto.
     */
    @Test
    public void testConstructorStoresValues() {

        ProductByWeight p = new ProductByWeight("W1", "Carne", 180.0);
        assertEquals("W1", p.getId());
        assertEquals("Carne", p.getName());
        assertEquals(180.0, p.getPricePerKg(), 0.0001);
    }

    /**
     * Verifica que el constructor rechace valores de precio negativos lanzando una excepción {@link IllegalArgumentException}.
     * 
     */
    @Test
    public void testConstructorRejectsNegativePrice() {

        assertThrows(IllegalArgumentException.class, () -> { new ProductByWeight("W1", "Carne", -30.0); });
    }

    /**
     * 
     * Verifica que el cálculo del precio funcione correctamente, multiplicando el precio por kilogramo por la cantidad solicitada.
     */
    @Test

    public void testCalculatePrice() {
        
        ProductByWeight p = new ProductByWeight("W2", "Carne", 200.0);
        assertEquals(300.0, p.calculatePrice(1.5), 0.0001);
    }

    /**
     * 
     * Verifica que {@code calculatePrice()} rechace cantidades negativas lanzando una excepción {@link IllegalArgumentException}.
     */
    @Test
    public void testCalculatePriceRejectsNegativeQuantity() {

        ProductByWeight p = new ProductByWeight("W3", "Carne", 250.0);

        assertThrows(IllegalArgumentException.class, () -> { p.calculatePrice(-2); });

    }

    /**
     * 
     * Verifica que dos productos con el mismo ID se consideren iguales, independientemente del nombre o el precio.
     */
    @Test
    public void testEqualsUsesId() {

        ProductByWeight p1 = new ProductByWeight("X1", "Algo", 10.0);
        ProductByWeight p2 = new ProductByWeight("X1", "Otro", 999.0);
        assertEquals(p1, p2);

        assertEquals(p1.hashCode(), p2.hashCode());
    }


    /**
     * 
     * Verifica que productos con distintos IDs no sean considerados iguales.
     */
    @Test
    public void testNotEqualsDifferentIds() {

        ProductByWeight p1 = new ProductByWeight("X1", "Producto", 50.0);
        ProductByWeight p2 = new ProductByWeight("X2", "Producto", 50.0);

        assertNotEquals(p1, p2);

    }

}