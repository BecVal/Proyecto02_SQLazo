package mx.unam.ciencias.myp.butchery.test.model.patrones.state;
import mx.unam.ciencias.myp.butchery.model.patrones.state.PendingState;
import mx.unam.ciencias.myp.butchery.model.patrones.state.PaidState;
import mx.unam.ciencias.myp.butchery.model.patrones.state.CanceledState;

import mx.unam.ciencias.myp.butchery.model.domain.Sale;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.Product;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.ProductByUnit;
import mx.unam.ciencias.myp.butchery.model.patrones.strategy.NoDiscount;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class PendingStateTest {

    private static final PrintStream REAL_OUT = System.out;

    @BeforeEach
    public void silenceOutput() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
    }

    @AfterEach
    public void restoreOutput() {
        System.setOut(REAL_OUT);
    }


    @Test
    public void testAddProductAddsCorrectQuantity() {
        Sale sale = new Sale();
        PendingState state = new PendingState();
        Product p = new ProductByUnit("1", "Carne", 10);

        state.addProduct(sale, p, 2);
        state.addProduct(sale, p, 3);

        assertEquals(5.0, sale.getItems().get(p));
    }

    @Test
    public void testApplyDiscountUsesStrategy() {
        Sale sale = new Sale();
        PendingState state = new PendingState();

        Product p = new ProductByUnit("1", "Carne", 20);
        state.addProduct(sale, p, 2);

        sale.setStrategy(new NoDiscount());
        state.applyDiscount(sale);

        assertEquals(40.0, sale.getTotal());
    }

    @Test
    public void testFinalizeChangesStateToPaid() {
        Sale sale = new Sale();
        PendingState state = new PendingState();

        state.finalizeSale(sale);

        assertTrue(sale.getState() instanceof PaidState);
    }

    @Test
    public void testCancelChangesStateToCanceled() {
        Sale sale = new Sale();
        PendingState state = new PendingState();

        state.cancelSale(sale);

        assertTrue(sale.getState() instanceof CanceledState);
    }
}
