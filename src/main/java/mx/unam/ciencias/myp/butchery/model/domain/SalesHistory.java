package mx.unam.ciencias.myp.butchery.model.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase que mantiene el historial de ventas realizadas.
 * 
 * @author Luis
 */
public class SalesHistory {

    private final List<Sale> sales;

    public SalesHistory() {
        this.sales = new ArrayList<>();
    }

    /**
     * Añade una venta al historial.
     * @param sale venta finalizada a agregar (no debe ser null)
     */
    public void addSale(Sale sale) {
        if (sale == null) throw new IllegalArgumentException("sale cannot be null");
        sales.add(sale);
    }

    /**
     * Devuelve una lista no modificable con las ventas registradas.
     * @return lista de ventas
     */
    public List<Sale> getSales() {
        return Collections.unmodifiableList(sales);
    }

    /**
     * Calcula los ingresos totales acumulados en el historial.
     * @return suma de totales de ventas
     */
    public double getTotalRevenue() {
        double sum = 0.0;
        for (Sale s : sales) {
            sum += s.getTotal();
        }
        return sum;
    }
}
