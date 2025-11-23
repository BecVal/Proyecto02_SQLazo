package mx.unam.ciencias.myp.butchery.test.model.patrones.factory;
import mx.unam.ciencias.myp.butchery.DatabaseManager;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.ProductByUnit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;

/**
 * Pruebas unitarias para la clase {@link ProductByUnit}.
 * <p>
 * Estas pruebas validan el correcto almacenamiento de atributos, las reglas de negocio relacionadas con precios, el cálculo del costo según la cantidad, y la correcta implementación de {@code equals()} y {@code hashCode()} basados en el identificador del producto.
 * 
 * </p>
 */
public class ProductByUnitTest {

    /**
     * 
     * Limpia la tabla {@code inventory} después de cada prueba para garantizar un entorno consistente y sin residuos de datos.
     */
    @AfterEach
    public void clean() throws Exception {
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM inventory;");
        }
    }

    /**
     * Verifica que el constructor almacene correctamente el identificador, nombre y precio del producto.
     * 
     */
    @Test
    public void testConstructorStoresValues() {

        ProductByUnit p = new ProductByUnit("A1", "Carne", 15.0);

        assertEquals("A1", p.getId());
        assertEquals("Carne", p.getName());
        assertEquals(15.0, p.getPricePerUnit(), 0.0001);
    }

    /**
     * 
     * Verifica que el constructor rechace valores negativos para el precio por unidad lanzando una excepción.
     */
    @Test

    public void testConstructorRejectsNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> { new ProductByUnit("A1", "Fallido", -5.0); });

    }

    /**
     * 
     * Verifica que el método {@code calculatePrice()} calcule correctamente el costo total multiplicando el precio por unidad por la cantidad.
     * 
     */
    @Test
    public void testCalculatePrice() {

        ProductByUnit p = new ProductByUnit("B2", "Costilla", 20.0);
        assertEquals(100.0, p.calculatePrice(5), 0.0001);
    }

    /**
     * Verifica que {@code calculatePrice()} rechace cantidades negativas lanzando una excepción.
     * 
     */
    @Test
    public void testCalculatePriceRejectsNegativeQuantity() {
        ProductByUnit p = new ProductByUnit("B2", "Costilla", 20.0);

        assertThrows(IllegalArgumentException.class, () -> { p.calculatePrice(-3); });

    }

    /**
     * Verifica que dos productos con el mismo ID sean considerados iguales, sin importar el nombre o el precio.
     */
    @Test
    public void testEqualsUsesId() {
        ProductByUnit p1 = new ProductByUnit("X1", "Corte 1", 10.0);
        ProductByUnit p2 = new ProductByUnit("X1", "Corte 2", 999.0);
        assertEquals(p1, p2);

        assertEquals(p1.hashCode(), p2.hashCode());
    }

    /**
     * Verifica que productos con distintos IDs se consideren diferentes.
     */
    @Test
    public void testNotEqualsDifferentIds() {
        ProductByUnit p1 = new ProductByUnit("X1", "Corte 1", 10.0);
        ProductByUnit p2 = new ProductByUnit("X2", "Corte 1", 10.0);

        assertNotEquals(p1, p2);

    }

}