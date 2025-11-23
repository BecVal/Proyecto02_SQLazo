package mx.unam.ciencias.myp.butchery.test.model;

import mx.unam.ciencias.myp.butchery.DatabaseManager;

import mx.unam.ciencias.myp.butchery.model.ModelFacade;

import mx.unam.ciencias.myp.butchery.model.domain.Sale;

import mx.unam.ciencias.myp.butchery.model.patrones.factory.*;

import mx.unam.ciencias.myp.butchery.model.patrones.state.PaidState;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Pruebas unitarias para {@link ModelFacade}, la capa que coordina las operaciones principales del modelo: productos, inventario y ventas.
 *
 * <p>Este conjunto de pruebas verifica que:</p>
 * <ul>
 *     <li>Los productos se agregan y se recuperan correctamente.</li>
 *     <li>Los cambios de precio se reflejan en el inventario.</li>
 *     <li>El procesamiento de una venta reduce el stock y la agrega al historial.</li>
 * 
 *     <li>El acceso a productos por índice se comporte correctamente.</li>
 * </ul>
 *
 * 
 * <p>También se incluye la limpieza del Singleton de Inventario y de la base de datos SQLite antes y después de cada prueba para asegurar aislamiento.</p>
 */
public class ModelFacadeTest {

    private static final PrintStream REAL_OUT = System.out;
    /**
     * Suprime temporalmente la salida estándar para evitar ruido durante las pruebas.
     */
    @BeforeEach
    public void silenceOutput() {

        System.setOut(new PrintStream(new ByteArrayOutputStream()));
    }

    /**
     * Restaura la salida estándar después de cada prueba.
     * 
     */
    @AfterEach
    public void restoreOutput() {
        System.setOut(REAL_OUT);
    }

    /**
     * Reinicia la instancia Singleton de {@code Inventory} mediante reflexión para garantizar que cada prueba comience con un inventario fresco.
     */
    @BeforeEach
    public void resetSingleton() throws Exception {

        var field = Class.forName("mx.unam.ciencias.myp.butchery.model.domain.Inventory").getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, null);

    }

    /**
     * 
     * Limpia la tabla {@code inventory} de SQLite antes de cada prueba.
     */
    @BeforeEach
    public void cleanDB() throws Exception {
        try (var conn = DatabaseManager.getConnection();

            var stmt = conn.createStatement()) {

            stmt.execute("DELETE FROM inventory;");
        }
    }

    /**
     * 
     * Limpia la tabla {@code inventory} de SQLite después de cada prueba.
     * 
     */
    @AfterEach

    public void clean() throws Exception {

        try (var conn = DatabaseManager.getConnection();
            var stmt = conn.createStatement()) {

            stmt.execute("DELETE FROM inventory;");
        }
    }

    /**
     * Verifica que un producto agregado por unidad pueda recuperarse correctamente por su nombre.
     * 
     */
    @Test
    public void testAddAndFindProduct() {

        ModelFacade model = new ModelFacade();

        model.addProductByUnit("111", "Carne", 10.0);

        var p = model.findProductByName("Carne");

        assertNotNull(p);
        assertEquals("Carne", p.getName());
    }

    /**
     * Verifica que un cambio de precio en un producto se aplique correctamente en el inventario.
     */
    @Test
    public void testUpdateProductPrice() {

        ModelFacade model = new ModelFacade();

        model.addProductByWeight("222", "Carne", 150.0);
        model.updateProductPrice("Carne", 200.0);

        var p = model.findProductByName("Carne");
        assertNotNull(p);

        assertEquals(200.0, ((ProductByWeight)p).getPricePerKg());
    }

    /**
     * Verifica que al procesar una venta:
     * 
     * <ul>
     *     <li>se reduzca el stock del producto vendido,</li>
     *     <li>se registre la venta en el historial.</li>
     * </ul>
     */
    @Test
    public void testProcessSaleReducesStockAndAddsToHistory() {

        ModelFacade model = new ModelFacade();
        model.addProductByUnit("333", "Carne", 5.0);

        model.addStockToProduct("Carne", 10.0);

        var sale = new Sale();
        Product p = model.findProductByName("Carne");
        sale.addProduct(p, 3.0);
        sale.setState(new PaidState());

        model.processSale(sale);

        double remaining = model.getStockByName("Carne");
        assertEquals(7.0, remaining);

        assertEquals(1, model.getSalesHistory().size());

    }

    /**
     * Verifica que la obtención de productos por índice en una lista ordenada funcione correctamente.
     */
    @Test
    public void testGetProductByIndex() {

        ModelFacade model = new ModelFacade();
        model.addProductByUnit("444", "Carne", 90.0);
        model.addProductByUnit("555", "Costilla", 70.0);

        var sorted = model.getProductsSorted();
        assertEquals(2, sorted.size());
        assertNotNull(model.getProductByIndex(0));

        assertNull(model.getProductByIndex(10));
    }

}



