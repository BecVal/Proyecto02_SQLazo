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

/**
 * Pruebas unitarias para {@link PendingState}, el estado inicial de una venta.
 * <p>
 * Estas pruebas verifican que:
 * <ul>
 *     <li>Los productos se agreguen correctamente mientras la venta está pendiente.</li>
 *     <li>La aplicación de descuentos delegue apropiadamente a la estrategia configurada.</li>
 * 
 *     <li>El estado cambie a {@link PaidState} tras finalizar una venta.</li>
 *     <li>El estado cambie a {@link CanceledState} cuando la venta es cancelada.</li>
 * </ul>
 * </p>
 */
public class PendingStateTest {

    private static final PrintStream REAL_OUT = System.out;

    /**
     * 
     * Suprime la salida estándar antes de cada prueba para evitar ruido en consola.
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
     * Verifica que {@link PendingState#addProduct(Sale, Product, double)} acumule correctamente la cantidad de un mismo producto.
     */
    @Test
    public void testAddProductAddsCorrectQuantity() {

        Sale sale = new Sale();

        PendingState state = new PendingState();
        Product p = new ProductByUnit("1", "Carne", 10);

        state.addProduct(sale, p, 2);
        state.addProduct(sale, p, 3);

        assertEquals(5.0, sale.getItems().get(p));
    }

    /**
     * 
     * Verifica que {@link PendingState#applyDiscount(Sale)} utilice la estrategia definida en la venta para calcular el total.
     */
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

    /**
     * 
     * Verifica que al finalizar una venta pendiente, el estado se actualice a {@link PaidState}.
     */
    @Test

    public void testFinalizeChangesStateToPaid() {

        Sale sale = new Sale();
        PendingState state = new PendingState();

        state.finalizeSale(sale);

        assertTrue(sale.getState() instanceof PaidState);

    }

    /**
     * 
     * Verifica que al cancelar una venta pendiente, el estado cambie a {@link CanceledState}.
     * 
     */
    @Test
    public void testCancelChangesStateToCanceled() {

        Sale sale = new Sale();
        PendingState state = new PendingState();
        state.cancelSale(sale);

        assertTrue(sale.getState() instanceof CanceledState);
    }
}