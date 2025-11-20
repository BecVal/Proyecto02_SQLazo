package mx.unam.ciencias.myp.butchery.test.model;
import mx.unam.ciencias.myp.butchery.model.ModelFacade;

import mx.unam.ciencias.myp.butchery.model.domain.Inventory;
import mx.unam.ciencias.myp.butchery.model.domain.Sale;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.*;
import mx.unam.ciencias.myp.butchery.model.patrones.state.PaidState;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

public class ModelFacadeTest {

    private static final PrintStream REAL_OUT = System.out;

    @BeforeEach
    public void silenceOutput() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
    }

    @AfterEach
    public void restoreOutput() {
        System.setOut(REAL_OUT);
    }

    @BeforeEach
    public void resetSingleton() throws Exception {
        Field instance = Inventory.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void testAddAndFindProduct() {
        ModelFacade model = new ModelFacade();
        model.addProductByUnit("111", "Carne", 10.0);

        var p = model.findProductByName("Carne");

        assertNotNull(p);
        assertEquals("Carne", p.getName());
    }

    @Test
    public void testUpdateProductPrice() {
        ModelFacade model = new ModelFacade();
        model.addProductByWeight("222", "Carne", 150.0);

        model.updateProductPrice("Carne", 200.0);

        var p = model.findProductByName("Carne");
        assertNotNull(p);
        assertEquals(200.0, ((ProductByWeight)p).getPricePerKg());
    }

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
