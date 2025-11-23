package mx.unam.ciencias.myp.butchery.test.model.patrones.strategy;

import mx.unam.ciencias.myp.butchery.model.patrones.strategy.PercentageDiscount;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para {@link PercentageDiscount}, una estrategia de descuento que reduce el total aplicando un porcentaje configurable.
 * <p>
 * 
 * Estas pruebas verifican que:
 * <ul>
 * 
 *     <li>El porcentaje indicado se aplique correctamente al total.</li>
 *     <li>Un descuento del 0% no altere la cantidad original.</li>
 *     <li>Un descuento del 100% reduzca el total a cero.</li>
 * 
 * </ul>
 * </p>
 */

public class PercentageDiscountTest {
    /**
     * Verifica que el descuento del porcentaje indicado se aplique correctamente al total.
     * 
     */
    @Test

    public void testAppliesCorrectPercentageDiscount() {

        PercentageDiscount d = new PercentageDiscount(0.25);
        double result = d.applyDiscount(200.0);

        assertEquals(150.0, result);
    }



    /**
     * Verifica que un descuento del 0% no modifique el total.
     */
    @Test
    public void testZeroDiscountDoesNothing() {

        PercentageDiscount d = new PercentageDiscount(0.0);
        double result = d.applyDiscount(500.0);
        assertEquals(500.0, result);

    }

    /**
     * 
     * Verifica que un descuento del 100% reduzca el total a cero.
     */
    @Test
    public void testFullDiscountMakesTotalZero() {
        
        PercentageDiscount d = new PercentageDiscount(1.0);
        double result = d.applyDiscount(350.0);
        assertEquals(0.0, result);
    }
}
