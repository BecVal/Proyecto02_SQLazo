package mx.unam.ciencias.myp.butchery.test.model.patrones.factory;
import mx.unam.ciencias.myp.butchery.DatabaseManager;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.ProductByUnit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;

public class ProductByUnitTest {

    @AfterEach
    public void clean() throws Exception {
        try (var conn = DatabaseManager.getConnection();
            var stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM inventory;");
        }
    }
    
    @Test
    public void testConstructorStoresValues() {
        ProductByUnit p = new ProductByUnit("A1", "Carne", 15.0);

        assertEquals("A1", p.getId());
        assertEquals("Carne", p.getName());
        assertEquals(15.0, p.getPricePerUnit(), 0.0001);
    }

    @Test
    public void testConstructorRejectsNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductByUnit("A1", "Fallido", -5.0);
        });
    }

    @Test
    public void testCalculatePrice() {
        ProductByUnit p = new ProductByUnit("B2", "Costilla", 20.0);

        assertEquals(100.0, p.calculatePrice(5), 0.0001);
    }

    @Test
    public void testCalculatePriceRejectsNegativeQuantity() {
        ProductByUnit p = new ProductByUnit("B2", "Costilla", 20.0);

        assertThrows(IllegalArgumentException.class, () -> {
            p.calculatePrice(-3);
        });
    }

    @Test
    public void testEqualsUsesId() {
        ProductByUnit p1 = new ProductByUnit("X1", "Corte 1", 10.0);
        ProductByUnit p2 = new ProductByUnit("X1", "Corte 2", 999.0);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testNotEqualsDifferentIds() {
        ProductByUnit p1 = new ProductByUnit("X1", "Corte 1", 10.0);
        ProductByUnit p2 = new ProductByUnit("X2", "Corte 1", 10.0);

        assertNotEquals(p1, p2);
    }
}
