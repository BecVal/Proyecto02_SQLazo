package mx.unam.ciencias.myp.butchery.test.model.domain;

import mx.unam.ciencias.myp.butchery.model.domain.Sale;
import mx.unam.ciencias.myp.butchery.model.domain.SalesHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link SalesHistory}.
 * <p>
 * Este conjunto de pruebas verifica el correcto funcionamiento del historial de ventas: almacenamiento de ventas, manejo de errores al agregar valores inválidos, protección contra modificaciones externas y cálculo del ingreso total generado.
 * </p>
 */
public class SalesHistoryTest {

    private SalesHistory history;
    /**
     * Inicializa un nuevo historial de ventas antes de cada prueba.
     * 
     */
    @BeforeEach
    public void setup() {
        history = new SalesHistory();
    }

    /**
     * Crea una venta de utilidad para las pruebas con un total específico.
     * @param total monto total de la venta.
     * @return una instancia de {@link Sale} configurada con ese total.
     */
    private Sale makeSale(double total) {
        Sale s = new Sale();
        s.setTotal(total);
        return s;
    }

    /**
     * Verifica que al agregar una venta válida, la lista interna aumente su tamaño y contenga la venta agregada.
     */
    @Test
    public void testAddSaleIncreasesList() {
        Sale s1 = makeSale(100.0);
        history.addSale(s1);

        assertEquals(1, history.getSales().size());
        assertTrue(history.getSales().contains(s1));
    }
    /**
     * Verifica que intentar agregar una venta nula arroje una excepción de tipo {@link IllegalArgumentException}.
     */
    @Test
    public void testAddSaleNullThrows() {

        assertThrows(IllegalArgumentException.class, () -> history.addSale(null));
    }

    /**
     * Verifica que la lista devuelta por {@code getSales()} sea inmodificable y arroje excepción si se intenta alterar.
     */
    @Test
    public void testGetSalesIsUnmodifiable() {

        Sale s1 = makeSale(50.0);
        history.addSale(s1);
        var list = history.getSales();

        assertThrows(UnsupportedOperationException.class, () -> list.add(makeSale(10.0)));

    }

    /**
     * Verifica que el cálculo del ingreso total sume correctamente los montos de todas las ventas registradas.
     */
    @Test
    public void testTotalRevenueSumsCorrectly() {
        history.addSale(makeSale(100.0));
        history.addSale(makeSale(200.0));
        history.addSale(makeSale(50.0));
        double total = history.getTotalRevenue();

        assertEquals(350.0, total, 0.0001);

    }


}