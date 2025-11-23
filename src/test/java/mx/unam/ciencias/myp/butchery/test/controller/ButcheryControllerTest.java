package mx.unam.ciencias.myp.butchery.test.controller;
import mx.unam.ciencias.myp.butchery.controller.ButcheryController;
import mx.unam.ciencias.myp.butchery.model.ModelFacade;
import mx.unam.ciencias.myp.butchery.model.domain.Sale;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;

/**
 * Pruebas unitarias para {@link ButcheryController}.
 * <p>
 * Este conjunto de pruebas valida que el controlador maneje correctamente los flujos principales relacionados con las ventas: creación de una venta, agregado de productos y procesamiento de la venta.
 * 
 * </p>
 * 
 * <p>Se utilizan stubs para aislar el comportamiento del controlador y evitar dependencias reales con la base de datos o el inventario persistente.</p>
 */
public class ButcheryControllerTest {
    private static final PrintStream REAL_OUT = System.out;
    /**
     * Redirige la salida estándar antes de cada prueba para evitar ruido generado por impresiones en consola.
     */
    @BeforeEach
    public void silenceOutput() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
    }

    /**
     * Restaura la salida estándar después de cada prueba.
     */
    @AfterEach
    public void restoreOutput() {

        System.setOut(REAL_OUT);
    }

    /**
     * Stub de {@link Product} para simular productos sin depender de las implementaciones reales del proyecto.
     */

    static class ProductStub implements Product {

        private String id;
        private String name;
        private double price;

        /**
         * Crea un producto de prueba.
         * @param id     identificador del producto
         * @param name   nombre del producto
         * @param price  precio unitario o por kg según corresponda
         */
        public ProductStub(String id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        @Override
        public String getName() {

            return name;
        }

        @Override
        public String getId() {
            return id;
        }

        /**
         * Devuelve el precio base del producto.
         *
         * @return precio asignado para esta instancia
         */
        public double getPrice() {
            return price;
        }

        /**
         * Calcula el costo total para una cantidad dada.
         *
         * @param quantity cantidad comprada
         * @return precio total
         * @throws IllegalArgumentException si la cantidad es negativa
         */
        @Override
        public double calculatePrice(double quantity) {
            if (quantity < 0) {

                throw new IllegalArgumentException("Cantidad negativa");
            }
            return price * quantity;
        }

    }
    /**
     * Stub de {@link ModelFacade} para simular el backend del sistema.
     * <p>
     * 
     * Permite controlar el inventario artificialmente y detectar si el método {@code processSale} fue llamado.
     * </p>
     */

    static class ModelFacadeStub extends ModelFacade {

        private List<Product> products = new ArrayList<>();
        private boolean processCalled = false;

        public ModelFacadeStub() {
            super();
        }

        /**
         * Agrega un producto ficticio al inventario simulado.
         *
         * @param p producto de prueba
         */
        public void addTestProduct(Product p) {
            products.add(p);
        }

        @Override
        public List<Product> getInventory() {
            return products;
        }

        @Override
        public Product getProductByIndex(int index) {
            if (index < 0 || index >= products.size()) return null;
            return products.get(index);
        }

        @Override

        public double getStockByProduct(Product p) {
            return 999.0;
        }
        @Override
        public void processSale(Sale sale) {
            processCalled = true;
        }

        /**
         * Indica si {@code processSale} fue invocado.
         *
         * @return {@code true} si el método fue ejecutado
         */
        public boolean wasProcessCalled() {
            return processCalled;
        }
    }

    /**
     * Verifica que {@link ButcheryController#beginSale()} cree correctamente una nueva instancia de {@link Sale}.
     */
    @Test
    public void testBeginSaleCreatesNewSale() {
        ModelFacadeStub model = new ModelFacadeStub();
        ButcheryController controller = new ButcheryController(model);
        assertNull(controller.getCurrentSale());
        controller.beginSale();

        assertNotNull(controller.getCurrentSale());
    }

    /**
     * Verifica que no se permita agregar productos si no existe
     * una venta activa.
     */
    @Test
    public void testAddProductFailsWithoutSale() {
        ModelFacadeStub model = new ModelFacadeStub();
        ButcheryController controller = new ButcheryController(model);
        String result = controller.addProductToCurrentSale(0, 1.0);
        assertEquals("No active sale.", result);
    }

    /**
     * Verifica que no se pueda seleccionar un producto usando un índice inválido.
     * 
     */
    @Test
    public void testAddProductInvalidProductIndex() {
        ModelFacadeStub model = new ModelFacadeStub();
        ButcheryController controller = new ButcheryController(model);
        controller.beginSale();

        String result = controller.addProductToCurrentSale(0, 1.0);

        assertEquals("Invalid product selection.", result);
    }

    /**
     * Verifica que una venta se procese correctamente y que el controlador delegue la operación al modelo.
     * 
     */
    @Test
    public void testPerformSaleProcessesCorrectly() {
        ModelFacadeStub model = new ModelFacadeStub();
        ButcheryController controller = new ButcheryController(model);

        Sale sale = new Sale();
        ProductStub p = new ProductStub("1", "Test", 10.0);

        model.addTestProduct(p);
        sale.addProduct(p, 2.0);
        String msg = controller.performSale(sale, false, 0.0);
        assertTrue(msg.contains("Registered sale"));
        assertTrue(model.wasProcessCalled());
    }
    
}