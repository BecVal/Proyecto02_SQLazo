package mx.unam.ciencias.myp.butchery.test.model.patrones.strategy;
import mx.unam.ciencias.myp.butchery.model.patrones.strategy.FrequentCustomerDiscount;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FrequentCustomerDiscountTest {

    @Test
    public void testAppliesTenPercentDiscount() {
        FrequentCustomerDiscount d = new FrequentCustomerDiscount();
        double result = d.applyDiscount(100.0);
        assertEquals(90.0, result);
    }

    @Test
    public void testZeroTotalStaysZero() {
        FrequentCustomerDiscount d = new FrequentCustomerDiscount();
        double result = d.applyDiscount(0.0);
        assertEquals(0.0, result);
    }

    @Test
    public void testWorksWithLargeNumbers() {
        FrequentCustomerDiscount d = new FrequentCustomerDiscount();
        double result = d.applyDiscount(10000.0);
        assertEquals(9000.0, result);
    }
}
