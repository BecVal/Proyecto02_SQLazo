package mx.unam.ciencias.myp.butchery.test.model.domain;
import mx.unam.ciencias.myp.butchery.model.domain.Sale;

import mx.unam.ciencias.myp.butchery.model.patrones.factory.Product;

import mx.unam.ciencias.myp.butchery.model.patrones.factory.ProductByUnit;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.ProductByWeight;
import mx.unam.ciencias.myp.butchery.model.patrones.strategy.NoDiscount;
import mx.unam.ciencias.myp.butchery.model.patrones.state.ISaleState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link Sale}.
 * <p>
 * Se validan aspectos fundamentales del funcionamiento de una venta: estado inicial, manejo de total, asignación de estrategia de descuento, actualización de estado, y cálculo del total sin descuentos.
 * 
 * </p>
 * 
 */

public class SaleTest {
    private Sale sale;

    /**
     * Inicializa una nueva venta antes de cada prueba.
     * 
     */
    @BeforeEach

    public void setup() {
        sale = new Sale();
    }

    /**
     * Verifica que el constructor inicialice correctamente la venta: sin productos, total en cero, estrategia por defecto y estado no nulo.
     */
    @Test
    public void testConstructorInitialState() {
        assertTrue(sale.getItems().isEmpty());
        assertEquals(0.0, sale.getTotal(), 0.0001);

        assertTrue(sale.getStrategy() instanceof NoDiscount);
        assertNotNull(sale.getState());
    }

    /**
     * Verifica que el método {@code setTotal()} actualice correctamente el total de la venta.
     */
    @Test
    public void testSetAndGetTotal() {
        sale.setTotal(123.45);
        assertEquals(123.45, sale.getTotal(), 0.0001);

    }



    /**
     * 
     * Verifica que pueda asignarse una estrategia de descuento y que esta se recupere correctamente.
     */
    @Test
    public void testSetAndGetStrategy() {
        NoDiscount nd = new NoDiscount();
        sale.setStrategy(nd);

        assertEquals(nd, sale.getStrategy());
    }

    /**
     * Verifica que pueda asignarse un nuevo estado a la venta y que este se recupere adecuadamente.
     */
    @Test
    public void testSetAndGetState() {
        ISaleState dummy = new DummyState();
        sale.setState(dummy);

        assertEquals(dummy, sale.getState());
    }

    /**
     * Verifica que el cálculo del total sin aplicar descuentos sea correcto con productos por unidad y por peso.
     * 
     */
    @Test
    public void testCalculateTotalWithoutDiscount() {
        ProductByUnit huevo = new ProductByUnit("1", "Huevo", 3.0);

        ProductByWeight bistec = new ProductByWeight("2", "Bistec", 150.0);

        sale.getItems().put(huevo, 4.0);
        sale.getItems().put(bistec, 0.5);

        assertEquals(87.0, sale.calculateTotalWithoutDiscount(), 0.0001);

    }

    /**
     * Implementación mínima de {@link ISaleState} utilizada como estado ficticio para validar el comportamiento de asignación de estado dentro de las pruebas.
     * 
     */

    private static class DummyState implements ISaleState {

        @Override
        public void addProduct(Sale sale, Product product, double quantity) {

            sale.getItems().put(product, quantity);
        }

        @Override
        public void applyDiscount(Sale sale) {
            double base = sale.calculateTotalWithoutDiscount();
            sale.setTotal(base);
        }

        @Override
        public void finalizeSale(Sale sale) {
            sale.setTotal(999.0);
        }

        @Override
        public void cancelSale(Sale sale) {
            sale.setTotal(0.0);
            
        }
    }

}