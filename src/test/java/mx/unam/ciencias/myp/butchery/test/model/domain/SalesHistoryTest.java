package mx.unam.ciencias.myp.butchery.test.model.domain;
import mx.unam.ciencias.myp.butchery.model.domain.Sale;
import mx.unam.ciencias.myp.butchery.model.domain.SalesHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class SalesHistoryTest {

    private SalesHistory history;

    @BeforeEach
    public void setup() {
        history = new SalesHistory();
    }

    private Sale makeSale(double total) {
        Sale s = new Sale();
        s.setTotal(total);
        return s;
    }

    @Test
    public void testAddSaleIncreasesList() {
        Sale s1 = makeSale(100.0);

        history.addSale(s1);

        assertEquals(1, history.getSales().size());
        assertTrue(history.getSales().contains(s1));
    }

    @Test
    public void testAddSaleNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> history.addSale(null));
    }

    @Test
    public void testGetSalesIsUnmodifiable() {
        Sale s1 = makeSale(50.0);
        history.addSale(s1);

        var list = history.getSales();

        assertThrows(UnsupportedOperationException.class,
                () -> list.add(makeSale(10.0)));
    }

    @Test
    public void testTotalRevenueSumsCorrectly() {
        history.addSale(makeSale(100.0));
        history.addSale(makeSale(200.0));
        history.addSale(makeSale(50.0));

        double total = history.getTotalRevenue();

        assertEquals(350.0, total, 0.0001);
    }
}
