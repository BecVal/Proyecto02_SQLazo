package mx.unam.ciencias.myp.butchery.test.model.domain;
import mx.unam.ciencias.myp.butchery.model.domain.Inventory;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.Product;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.ProductByUnit;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.ProductByWeight;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    @BeforeEach
    public void resetInventory() {
        Inventory inv = Inventory.getInstance();
    
        var productos = new java.util.ArrayList<>(inv.getInventory().keySet());
    
        for (Product p : productos) {
            inv.removeProductByName(p.getName());
        }
    }
    
    @Test
    public void testAddProductByUnitAddsCorrectly() {
        Inventory inv = Inventory.getInstance();

        inv.addProductByUnit("1", "Carne", 15.0);

        Product p = inv.getProductByName("Carne");
        assertNotNull(p);
        assertTrue(p instanceof ProductByUnit);
    }

    @Test
    public void testAddProductByWeightAddsCorrectly() {
        Inventory inv = Inventory.getInstance();

        inv.addProductByWeight("2", "Carne", 250.0);

        Product p = inv.getProductByName("Carne");
        assertNotNull(p);
        assertTrue(p instanceof ProductByWeight);
    }


    @Test
    public void testAddStockByUnitIncreasesQuantity() {
        Inventory inv = Inventory.getInstance();
        inv.addProductByUnit("1", "Carne", 15.0);

        Product p = inv.getProductByName("Carne");

        inv.addStockByUnit(p, 5.0);

        assertEquals(5.0, inv.getStock(p));
    }


    @Test
    public void testReduceStockWorks() {
        Inventory inv = Inventory.getInstance();
        inv.addProductByUnit("1", "Carne", 15.0);
        Product p = inv.getProductByName("Carne");

        inv.addStockByUnit(p, 10.0);
        inv.reduceStock(p, 4.0);

        assertEquals(6.0, inv.getStock(p));
    }

    @Test
    public void testUpdateProductNameWorks() {
        Inventory inv = Inventory.getInstance();
        inv.addProductByUnit("1", "Carne", 15.0);

        inv.updateProductName("Carne", "Costilla");

        assertNull(inv.getProductByName("Carne"));
        assertNotNull(inv.getProductByName("Costilla"));
    }


    @Test
    public void testUpdateProductPriceWorks() {
        Inventory inv = Inventory.getInstance();
        inv.addProductByWeight("2", "Carne", 250.0);

        inv.updateProductPrice("Carne", 300.0);

        Product updated = inv.getProductByName("Carne");
        assertTrue(updated instanceof ProductByWeight);
        assertEquals(300.0, ((ProductByWeight) updated).getPricePerKg());
    }


    @Test
    public void testRemoveProductByName() {
        Inventory inv = Inventory.getInstance();
        inv.addProductByUnit("3", "Pechuga", 80.0);

        assertTrue(inv.removeProductByName("Pechuga"));
        assertNull(inv.getProductByName("Pechuga"));
    }
}
