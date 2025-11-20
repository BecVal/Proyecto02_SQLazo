package mx.unam.ciencias.myp.butchery.test.model.patrones.factory;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.ProductByWeight;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ProductByWeightTest {

    @Test
    public void testConstructorStoresValues() {
        ProductByWeight p = new ProductByWeight("W1", "Carne", 180.0);

        assertEquals("W1", p.getId());
        assertEquals("Carne", p.getName());
        assertEquals(180.0, p.getPricePerKg(), 0.0001);
    }

    @Test
    public void testConstructorRejectsNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductByWeight("W1", "Carne", -30.0);
        });
    }

    @Test
    public void testCalculatePrice() {
        ProductByWeight p = new ProductByWeight("W2", "Carne", 200.0);

        assertEquals(300.0, p.calculatePrice(1.5), 0.0001);
    }

    @Test
    public void testCalculatePriceRejectsNegativeQuantity() {
        ProductByWeight p = new ProductByWeight("W3", "Carne", 250.0);

        assertThrows(IllegalArgumentException.class, () -> {
            p.calculatePrice(-2);
        });
    }

    @Test
    public void testEqualsUsesId() {
        ProductByWeight p1 = new ProductByWeight("X1", "Algo", 10.0);
        ProductByWeight p2 = new ProductByWeight("X1", "Otro", 999.0);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testNotEqualsDifferentIds() {
        ProductByWeight p1 = new ProductByWeight("X1", "Producto", 50.0);
        ProductByWeight p2 = new ProductByWeight("X2", "Producto", 50.0);

        assertNotEquals(p1, p2);
    }
}
