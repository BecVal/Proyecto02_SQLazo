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

public class ButcheryControllerTest {

    private static final PrintStream REAL_OUT = System.out;

    @BeforeEach
    public void silenceOutput() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
    }

    @AfterEach
    public void restoreOutput() {
        System.setOut(REAL_OUT);
    }


    static class ProductStub implements Product {

        private String id;
        private String name;
        private double price;
    
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
    
        public double getPrice() {
            return price;
        }
    
        @Override
        public double calculatePrice(double quantity) {
            if (quantity < 0) {
                throw new IllegalArgumentException("Cantidad negativa");
            }
            return price * quantity;
        }
    }
    

    static class ModelFacadeStub extends ModelFacade {

        private List<Product> products = new ArrayList<>();
        private boolean processCalled = false;

        public ModelFacadeStub() {
            super();
        }

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

        public boolean wasProcessCalled() {
            return processCalled;
        }
    }

    @Test
    public void testBeginSaleCreatesNewSale() {
        ModelFacadeStub model = new ModelFacadeStub();
        ButcheryController controller = new ButcheryController(model);

        assertNull(controller.getCurrentSale());

        controller.beginSale();

        assertNotNull(controller.getCurrentSale());
    }

    @Test
    public void testAddProductFailsWithoutSale() {
        ModelFacadeStub model = new ModelFacadeStub();
        ButcheryController controller = new ButcheryController(model);

        String result = controller.addProductToCurrentSale(0, 1.0);

        assertEquals("No active sale.", result);
    }

    @Test
    public void testAddProductInvalidProductIndex() {
        ModelFacadeStub model = new ModelFacadeStub();
        ButcheryController controller = new ButcheryController(model);

        controller.beginSale();

        String result = controller.addProductToCurrentSale(0, 1.0);

        assertEquals("Invalid product selection.", result);
    }

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
