package mx.unam.ciencias.myp.butchery.test.model.domain;
import mx.unam.ciencias.myp.butchery.DatabaseManager;

import mx.unam.ciencias.myp.butchery.model.domain.Inventory;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.Product;

import mx.unam.ciencias.myp.butchery.model.patrones.factory.ProductByUnit;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.ProductByWeight;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link Inventory}.
 * <p>
 * Este conjunto de pruebas verifica el funcionamiento de las operaciones principales del inventario: registro de productos, actualización de valores, manejo del stock, renombrado, eliminación y recuperación desde la instancia Singleton.
 * </p>
 * <p>La base de datos SQLite utilizada es limpiada entre pruebas para evitar contaminación entre casos y asegurar consistencia en cada ejecución.</p>
 */
public class InventoryTest {

    /**
     * 
     * Limpia el inventario en memoria antes de cada prueba, eliminando todos los productos registrados en la instancia Singleton.
     * 
     */
    @BeforeEach

    public void resetInventory() {

        Inventory inv = Inventory.getInstance();
        var productos = new java.util.ArrayList<>(inv.getInventory().keySet());
        for (Product p : productos) {
            inv.removeProductByName(p.getName());
        }
    }

    /**
     * Limpia la tabla de la base de datos después de cada prueba, asegurando que no queden registros persistentes.
     */
    @AfterEach
    public void clean() throws Exception {
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM inventory;");
        }
    }

    /**
     * Verifica que un producto vendido por unidad se agregue correctamente al inventario.
     */
    @Test
    public void testAddProductByUnitAddsCorrectly() {
        Inventory inv = Inventory.getInstance();
        inv.addProductByUnit("1", "Carne", 15.0);
        Product p = inv.getProductByName("Carne");
        assertNotNull(p);
        assertTrue(p instanceof ProductByUnit);
    }

    /**
     * Verifica que un producto vendido por peso se agregue correctamente al inventario.
     */
    @Test
    public void testAddProductByWeightAddsCorrectly() {

        Inventory inv = Inventory.getInstance();

        inv.addProductByWeight("2", "Carne", 250.0);
        Product p = inv.getProductByName("Carne");
        assertNotNull(p);
        assertTrue(p instanceof ProductByWeight);
    }

    /**
     * Verifica que el método {@code addStockByUnit} incremente el stock correctamente para un producto por unidad.
     */
    @Test
    public void testAddStockByUnitIncreasesQuantity() {
        Inventory inv = Inventory.getInstance();
        inv.addProductByUnit("1", "Carne", 15.0);
        Product p = inv.getProductByName("Carne");

        inv.addStockByUnit(p, 5.0);
        assertEquals(5.0, inv.getStock(p));
    }

    /**
     * 
     * Verifica que {@code reduceStock} disminuya la cantidad disponible del producto correctamente.
     */
    @Test
    public void testReduceStockWorks() {
        Inventory inv = Inventory.getInstance();
        inv.addProductByUnit("1", "Carne", 15.0);
        Product p = inv.getProductByName("Carne");
        inv.addStockByUnit(p, 10.0);

        inv.reduceStock(p, 4.0);
        assertEquals(6.0, inv.getStock(p));
    }

    /**
     * Verifica que el renombrado de productos actualice correctamente la clave de acceso y que el producto anterior deje de existir.
     */
    @Test

    public void testUpdateProductNameWorks() {
        Inventory inv = Inventory.getInstance();
        inv.addProductByUnit("1", "Carne", 15.0);
        inv.updateProductName("Carne", "Costilla");

        assertNull(inv.getProductByName("Carne"));
        assertNotNull(inv.getProductByName("Costilla"));
    }
    /**
     * Verifica que el precio de un producto vendido por peso pueda ser actualizado correctamente.
     * 
     */
    @Test
    public void testUpdateProductPriceWorks() {

        Inventory inv = Inventory.getInstance();
        inv.addProductByWeight("2", "Carne", 250.0);
        inv.updateProductPrice("Carne", 300.0);
        Product updated = inv.getProductByName("Carne");
        assertTrue(updated instanceof ProductByWeight);
        assertEquals(300.0, ((ProductByWeight) updated).getPricePerKg());

    }

    /**
     * Verifica que {@code removeProductByName} elimine correctamente un producto y que posteriormente no pueda ser encontrado.
     * 
     */
    @Test
    public void testRemoveProductByName() {

        Inventory inv = Inventory.getInstance();
        inv.addProductByUnit("3", "Pechuga", 80.0);
        assertTrue(inv.removeProductByName("Pechuga"));
        assertNull(inv.getProductByName("Pechuga"));
    }
}

