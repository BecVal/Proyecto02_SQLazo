package mx.unam.ciencias.myp.butchery.test.model.patrones.factory;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.Product;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.ProductByUnit;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.ProductByWeight;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.ProductFactory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductFactoryTest {

    @Test
    public void testCreateProductByUnit() {
        ProductFactory f = new ProductFactory();
        Product p = f.createProduct("ID1", ProductFactory.ProductType.BY_UNIT, "Carne", 15.0);

        assertTrue(p instanceof ProductByUnit);
        assertEquals("Carne", p.getName());
        assertEquals("ID1", p.getId());
        assertEquals(15.0, ((ProductByUnit) p).getPricePerUnit());
    }

    @Test
    public void testCreateProductByWeight() {
        ProductFactory f = new ProductFactory();
        Product p = f.createProduct("ID2", ProductFactory.ProductType.BY_WEIGHT, "Carne", 200.0);

        assertTrue(p instanceof ProductByWeight);
        assertEquals("Carne", p.getName());
        assertEquals("ID2", p.getId());
        assertEquals(200.0, ((ProductByWeight) p).getPricePerKg());
    }

    @Test
    public void testCreateProductDefault() {
        ProductFactory f = new ProductFactory();
        Product p = f.createProduct("X9", ProductFactory.ProductType.BY_UNIT);

        assertTrue(p instanceof ProductByUnit);
        assertEquals("X9", p.getName());
        assertEquals("X9", p.getId());
        assertEquals(0.0, ((ProductByUnit) p).getPricePerUnit());
    }

}
