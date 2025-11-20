package mx.unam.ciencias.myp.butchery.test.model.patrones.strategy;
import mx.unam.ciencias.myp.butchery.model.patrones.strategy.PercentageDiscount;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PercentageDiscountTest {

    @Test
    public void testAppliesCorrectPercentageDiscount() {
        PercentageDiscount d = new PercentageDiscount(0.25);
        double result = d.applyDiscount(200.0);
        assertEquals(150.0, result);
    }

    @Test
    public void testZeroDiscountDoesNothing() {
        PercentageDiscount d = new PercentageDiscount(0.0);
        double result = d.applyDiscount(500.0);
        assertEquals(500.0, result);
    }

    @Test
    public void testFullDiscountMakesTotalZero() {
        PercentageDiscount d = new PercentageDiscount(1.0);
        double result = d.applyDiscount(350.0);
        assertEquals(0.0, result);
    }
}
