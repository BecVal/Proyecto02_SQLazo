package mx.unam.ciencias.myp.butchery.test.model.patrones.strategy;
import mx.unam.ciencias.myp.butchery.model.patrones.strategy.FrequentCustomerDiscount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 
 * Pruebas unitarias para {@link FrequentCustomerDiscount}, la estrategia que aplica un descuento del 10% al total de una venta.
 * <p>
 * 
 * Estas pruebas verifican que:
 * 
 * <ul>
 *     <li>El descuento del 10% se aplique correctamente a un total estándar.</li>
 *     <li>Un total de cero permanezca en cero tras aplicar el descuento.</li>
 * 
 *     <li>El cálculo funcione correctamente con cantidades grandes.</li>
 * </ul>
 * </p>
 * 
 */

public class FrequentCustomerDiscountTest {

    /**
     * Verifica que el descuento reduzca el total en exactamente un 10%.
     */
    @Test
    public void testAppliesTenPercentDiscount() {

        FrequentCustomerDiscount d = new FrequentCustomerDiscount();
        double result = d.applyDiscount(100.0);
        assertEquals(90.0, result);
    }



    /**
     * Verifica que si el total es cero, el descuento no altere el resultado.
     */
    @Test

    public void testZeroTotalStaysZero() {
        FrequentCustomerDiscount d = new FrequentCustomerDiscount();
        double result = d.applyDiscount(0.0);
        assertEquals(0.0, result);
    }

    /**
     * 
     * Verifica que la estrategia funcione correctamente con números grandes, manteniendo la precisión esperada.
     */
    @Test
    public void testWorksWithLargeNumbers() {

        FrequentCustomerDiscount d = new FrequentCustomerDiscount();
        double result = d.applyDiscount(10000.0);

        assertEquals(9000.0, result);



    }

}