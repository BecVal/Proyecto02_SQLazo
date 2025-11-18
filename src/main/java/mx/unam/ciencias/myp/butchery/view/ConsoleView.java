package mx.unam.ciencias.myp.butchery.view;

import mx.unam.ciencias.myp.butchery.controller.ButcheryController;
import mx.unam.ciencias.myp.butchery.model.patrones.factory.*;
import mx.unam.ciencias.myp.butchery.model.domain.Sale;

import java.util.List;
import java.util.Scanner;

/**
 * Vista de consola para la carnicería, que interactúa con el usuario
 * a través de la terminal.
 * 
 * @author Luis
 */
public class ConsoleView {
    private final ButcheryController controller;
    Scanner scanner = new Scanner(System.in);
    
    /**
     * Crea una vista de consola con el controlador proporcionado.
     * @param controller controlador de la carnicería (no debe ser null)
     */
    public ConsoleView(ButcheryController controller) {
        this.controller = controller;
    }
    
    /**
     * Clase que muestra el menú principal y maneja la navegación.
     */
    public void showMenu() {
        while (true) {
            System.out.println(
                            " ▄▄▄▄▄▄▄▄  ▄▄▄▄                ▄▄▄▄▄▄▄▄                                                             \r\n" + //
                            " ██▀▀▀▀▀▀  ▀▀██                ▀▀▀▀▀███                                                             \r\n" + //
                            " ██          ██                    ██▀    ▄█████▄   ██▄████  ██▄███▄    ▄█████▄  ████████   ▄████▄  \r\n" + //
                            " ███████     ██                  ▄██▀     ▀ ▄▄▄██   ██▀      ██▀  ▀██   ▀ ▄▄▄██      ▄█▀   ██▀  ▀██ \r\n" + //
                            " ██          ██                 ▄██      ▄██▀▀▀██   ██       ██    ██  ▄██▀▀▀██    ▄█▀     ██    ██ \r\n" + //
                            " ██▄▄▄▄▄▄    ██▄▄▄             ███▄▄▄▄▄  ██▄▄▄███   ██       ███▄▄██▀  ██▄▄▄███  ▄██▄▄▄▄▄  ▀██▄▄██▀ \r\n" + //
                            " ▀▀▀▀▀▀▀▀     ▀▀▀▀             ▀▀▀▀▀▀▀▀   ▀▀▀▀ ▀▀   ▀▀       ██ ▀▀▀     ▀▀▀▀ ▀▀  ▀▀▀▀▀▀▀▀    ▀▀▀▀   \r\n" + //
                            "                                                             ██                                     " + //
                            "");

            System.out.println(
                            "                        _____     _       _                  _           \r\n" + //
                            "                       | __  |_ _| |_ ___| |_ ___ ___    ___| |_ ___ ___ \r\n" + //
                            "                       | __ -| | |  _|  _|   | -_|  _|  |_ -|   | . | . |\r\n" + //
                            "                       |_____|___|_| |___|_|_|___|_|    |___|_|_|___|  _|\r\n" + //
                            "                                                                    |_|  \r\n" + //
                            "");
            System.out.println("=============================================================================================================");
            System.out.println("1. Manage Inventory");
            System.out.println("2. Manage Sales");
            System.out.println("3. Exit");

            String line = readLine("Choose option:");
            int opt;
            try {
                opt = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Enter a number.");
                continue;
            }

            switch (opt) {
                case 1:
                    manageInventory();
                    break;
                case 2:
                    manageSales();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    /**
     * Muestra el menú de gestión de inventario y maneja las opciones.
     */
    public void manageInventory() {
        while (true) {
            System.out.println("=============================================================================================================");
            System.out.println("Inventory:\n");
            System.out.println("1. Show Inventory");
            System.out.println("2. Register new product");
            System.out.println("3. Add stock to product");
            System.out.println("4. Modify product (name/price)");
            System.out.println("5. Delete product");
            System.out.println("6. Seed sample data (test)");
            System.out.println("7. Back to Main Menu");

            String line = readLine("Choose option:");
            int opt;
            try {
                opt = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Enter a number.");
                continue;
            }

            switch (opt) {
                case 1:
                    System.out.println("Showing inventory...");
                    showInventory();
                    break;
                case 2:
                    registerProduct();
                    break;
                case 3:
                    addStockToProduct();
                    break;
                case 4:
                    modifyProduct();
                    break;
                case 5:
                    deleteProduct();
                    break;
                case 6:
                    seedSampleData();
                    break;
                case 7:
                    System.out.println("Returning to Main Menu...");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    /**
     * La vista le pide al controlador agregar stock a un producto.
     */
    public void addStockToProduct() {
        System.out.println("=============================================================================================================");
        List<Product> products = controller.getProductsSorted();
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        System.out.println("Select product by number to add stock:");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            double qty = controller.getStockByProduct(p);
            String type = (p instanceof ProductByUnit) ? "BY_UNIT" : "BY_WEIGHT";
            double price = 0.0;
            if (p instanceof ProductByUnit)
                price = ((ProductByUnit)p).getPricePerUnit();
            else
                price = ((ProductByWeight)p).getPricePerKg();

            System.out.println(String.format("%d) %s | Qty: %.2f | Price: %.2f | %s", i+1, p.getName(), qty, price, type));
        }

        int sel = readInt("Select product number:") - 1;
        Product selProduct = controller.getProductByIndex(sel);
        if (selProduct == null) {
            System.out.println("Invalid selection.");
            return;
        }

        try {
            if (selProduct instanceof ProductByUnit) {
                int qty = readInt("Quantity (units) to add:");
                controller.addStockToProduct(selProduct.getName(), qty);
                System.out.println("Added " + qty + " units to " + selProduct.getName() + ".");
            } else {
                double kg = readDouble("Weight (kg) to add:");
                controller.addStockToProduct(selProduct.getName(), kg);
                System.out.println("Added " + kg + " kg to " + selProduct.getName() + ".");
            }
            double total = controller.getStockByProduct(selProduct);
            System.out.println(String.format("New stock for %s: %.2f", selProduct.getName(), total));
        } catch (Exception e) {
            System.out.println("Error adding stock: " + e.getMessage());
        }
    }

    /**
     * La vista le pide al controlador modificar un producto.
     */
    public void modifyProduct() {
        System.out.println("=============================================================================================================");
        List<Product> products = controller.getProductsSorted();
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        System.out.println("Select product by number to modify:");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            double qty = controller.getStockByProduct(p);
            String type = (p instanceof ProductByUnit) ? "BY_UNIT" : "BY_WEIGHT";
            double price = 0.0;
            if (p instanceof ProductByUnit)
                price = ((ProductByUnit)p).getPricePerUnit();
            else
                price = ((ProductByWeight)p).getPricePerKg();

            System.out.println(String.format("%d) %s | Qty: %.2f | Price: %.2f | %s", i+1, p.getName(), qty, price, type));
        }

        int sel = readInt("Select product number:") - 1;
        Product selProduct = controller.getProductByIndex(sel);
        if (selProduct == null) {
            System.out.println("Invalid selection.");
            return;
        }

        System.out.println("1. Change name");
        System.out.println("2. Change price");
        System.out.println("3. Cancel");
        int opt = readInt("Choose option:");
        try {
            switch (opt) {
                case 1:
                    String newName = readLine("New name:");
                    controller.updateProductName(selProduct.getName(), newName);
                    break;
                case 2:
                    double newPrice = readDouble("New price:");
                    controller.updateProductPrice(selProduct.getName(), newPrice);
                    break;
                case 3:
                    System.out.println("Cancelled.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } catch (Exception e) {
            System.out.println("Error modifying product: " + e.getMessage());
        }
    }

    /**
     * La vista le pide al controlador eliminar un producto.
     */
    public void deleteProduct() {
        System.out.println("=============================================================================================================");
        List<Product> products = controller.getProductsSorted();
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        System.out.println("Select product by number to delete:");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            double qty = controller.getStockByProduct(p);
            double price = (p instanceof ProductByUnit)
                ? ((ProductByUnit)p).getPricePerUnit()
                : ((ProductByWeight)p).getPricePerKg();
            System.out.println(String.format("%d) %s | Qty: %.2f | Price: %.2f", i+1, p.getName(), qty, price));
        }

        int sel = readInt("Select product number to delete:") - 1;
        boolean removed = controller.removeProductByIndex(sel);
        if (!removed) System.out.println("Invalid selection or deletion failed.");
    }

    /**
     * La vista le pide al controlador sembrar datos de prueba.
     */
    public void seedSampleData() {
        System.out.println("=============================================================================================================");
        System.out.println("Seeding sample butcher data (this is for tests)...");
        try {
            controller.seedSampleData();
            System.out.println("Sample data seeded.");
        } catch (Exception e) {
            System.out.println("\nError seeding data: " + e.getMessage());
        }
    }

    /**
     * Muestra el menú de gestión de ventas y maneja las opciones.
     */
    public void manageSales() {
        while (true) {
            System.out.println("=============================================================================================================");
            System.out.println("Sales Management:\n");
            System.out.println("1. Register new sale");
            System.out.println("2. Sales History");
            System.out.println("3. Show Inventory");
            System.out.println("4. Back to Main Menu");

            String line = readLine("Choose option:");
            int opt;
            try {
                opt = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Enter a number.");
                continue;
            }

            switch (opt) {
                case 1:
                    registerSale();
                    break;
                case 2:
                    System.out.println("Showing sales history...");
                    salesHistory();
                    break;
                case 3:
                    System.out.println("Showing inventory...");
                    showInventory();
                    break;
                case 4:
                    System.out.println("Returning to Main Menu...");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    /**
     * Menu para decidir el tipo de producto a registrar.
     */
    public void registerProduct() {
        while (true) {
            System.out.println("=============================================================================================================");
            System.out.println("Registering new product:\n");
            System.out.println("1. Add Product by Unit");
            System.out.println("2. Add Product by Weight");
            System.out.println("3. Back to Inventory Menu");

            String line = readLine("Choose option:");
            int opt;
            try {
                opt = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Enter a number.");
                continue;
            }

            switch (opt) {
                case 1:
                    registerProductByUnit();
                    break;
                case 2:
                    registerProductByWeight();
                    break;
                case 3:
                    System.out.println("Returning to Inventory Menu...");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    /**
     * Menu para registrar una nueva venta.
     * La vista le pide al controlador registrar una nueva venta.
     */
    public void registerSale() {
        System.out.println("=============================================================================================================");
        System.out.println("Registering new sale:\n");

        List<Product> products = controller.getProductsSorted();
        if (products.isEmpty()) {
            System.out.println("No products available to sell.");
            return;
        }
        
        controller.beginSale();
        while (true) {
            System.out.println("=============================================================================================================");
            System.out.println("Select product by number to add (0 to finish):\n");
            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                double qty = controller.getStockByProduct(p);
                String type = (p instanceof ProductByUnit) ? "BY_UNIT" : "BY_WEIGHT";
                double price = 0.0;
                if (p instanceof ProductByUnit)
                    price = ((ProductByUnit)p).getPricePerUnit();
                else
                    price = ((ProductByWeight)p).getPricePerKg();

                System.out.println(String.format("%d) %s | Stock: %.2f | Price: %.2f | %s", i+1, p.getName(), qty, price, type));
            }

            int sel = readInt("\nSelect product number (0 to finish):") - 1;
            if (sel < 0) {
                double subtotalNow = controller.getCurrentSubtotal();
                if (Math.abs(subtotalNow) < 0.0001) {
                    System.out.println("\nNo items added. Aborting sale.");
                    controller.cancelCurrentSale();
                    return;
                }
                break;
            }

            Product selProduct = controller.getProductByIndex(sel);
            if (selProduct == null) {
                System.out.println("\nInvalid selection.");
                continue;
            }

            if (selProduct instanceof ProductByUnit) {
                int quantity = readInt("Quantity (units) to add:");
                String err = controller.addProductToCurrentSale(sel, quantity);
                if (err != null) {
                    System.out.println(err);
                    continue;
                }
                System.out.println("Added " + quantity + " units of " + selProduct.getName());
            } else {
                double weight = readDouble("Weight (kg) to add:");
                String err = controller.addProductToCurrentSale(sel, weight);
                if (err != null) {
                    System.out.println(err);
                    continue;
                }
                System.out.println("Added " + weight + " kg of " + selProduct.getName());
            }
        }

        double subtotal = controller.getCurrentSubtotal();
        System.out.println(String.format("Subtotal: %.2f", subtotal));

        boolean isFrequent;
        while (true) {
            String frequentAns = readLine("Is this a frequent customer? (y/n):");
            if (frequentAns.equalsIgnoreCase("y") || frequentAns.equalsIgnoreCase("yes")) {
                isFrequent = true;
                break;
            }
            if (frequentAns.equalsIgnoreCase("n") || frequentAns.equalsIgnoreCase("no")) {
                isFrequent = false;
                break;
            }
            System.out.println("Invalid input. Please answer 'y' or 'n'.");
        }

        double discount = 0.0;
        if (!isFrequent) {
            discount = readDouble("Discount Strategy (0-100%):");
        }

        System.out.println("\n1. Finalize and process sale\n2. Cancel sale");
        int opt = readInt("Choose option:");
        while (opt != 1 && opt != 2) {
            System.out.println("\nInvalid option. Please enter 1 to finalize or 2 to cancel.");
            opt = readInt("Choose option:");
        }

        if (opt == 1) {
            String result = controller.finishCurrentSale(isFrequent, discount);
            System.out.println(result);
        } else { // opt == 2
            controller.cancelCurrentSale();
            System.out.println("Sale canceled.");
        }

    }

    /**
     * Muestra el historial de ventas.
     * La vista le pide al controlador el historial de ventas.
     */
    public void salesHistory() {
        System.out.println("=============================================================================================================");
        List<?> history = controller.getSalesHistory();
        if (history == null || history.isEmpty()) {
            System.out.println("No sales recorded yet.");
            return;
        }

        int idx = 1;
        for (Object s : history) {
            System.out.println("Sale #" + (idx++) + ": " + s.toString());
            
            try {
                Sale sale = (Sale)s;
                System.out.println("  Items: " + sale.getItems());
                System.out.println(String.format("  Total: %.2f", sale.getTotal()));
            } catch (Exception e) {
                
            }
        }
        System.out.println(String.format("Total revenue: %.2f", controller.getTotalRevenue()));
        
    }

    /**
     * Menu para registrar un nuevo producto por unidad.
     * La vista le pide al controlador registrar un nuevo producto por unidad.
     */
    public void registerProductByUnit() {
        System.out.println("=============================================================================================================");
        System.out.println("Registering product by unit:\n");

        String name = readLine("Enter product name:");
        double pricePerUnit = readDouble("Enter price per unit:");

        String id = java.util.UUID.randomUUID().toString();
        controller.registerProductByUnit(id, name, pricePerUnit);
    }

    /**
     * Menu para registrar un nuevo producto por peso.
     * La vista le pide al controlador registrar un nuevo producto por peso.
     */
    public void registerProductByWeight() {
        System.out.println("=============================================================================================================");
        System.out.println("Registering product by weight:\n");

        String name = readLine("Enter product name:");
        double pricePerKg = readDouble("Enter price per kilogram:");

        String id = java.util.UUID.randomUUID().toString();
        controller.registerProductByWeight(id, name, pricePerKg);
    }

    /**
     * Muestra el inventario actual.
     * La vista le pide al controlador la lista de productos y sus existencias.
     */
    public void showInventory() {
        System.out.println("=============================================================================================================");
        List<Product> products = controller.getProductsSorted();
        if (products.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        System.out.println("Displaying inventory items...");
        for (Product p : products) {
            double qty = controller.getStockByProduct(p);
            double price = 0.0;
            String type = "?";
            if (p instanceof ProductByUnit) {
                price = ((ProductByUnit)p).getPricePerUnit();
                type = "BY_UNIT";
            } else if (p instanceof ProductByWeight) {
                price = ((ProductByWeight)p).getPricePerKg();
                type = "BY_WEIGHT";
            }
            System.out.println(String.format("- %s | Qty: %.2f | Price: %.2f | %s", p.getName(), qty, price, type));
        }
    }

    /**
     * Lee una línea de entrada del usuario con el prompt dado.
     * @param prompt mensaje a mostrar al usuario
     * @return línea leída del usuario
     */
    private String readLine(String prompt) {
        System.out.print(prompt + " ");
        String line = scanner.nextLine();
        while (line == null || line.trim().isEmpty()) {
            System.out.print(prompt + " ");
            line = scanner.nextLine();
        }
        return line.trim();
    }

    /**
     * Lee un valor double de entrada del usuario con el prompt dado.
     * @param prompt mensaje a mostrar al usuario
     * @return valor double leído del usuario
     */
    private double readDouble(String prompt) {
        while (true) {
            String l = readLine(prompt);
            try {
                double v = Double.parseDouble(l);
                if (v < 0) {
                    System.out.println("Value must be >= 0");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Lee un valor entero de entrada del usuario con el prompt dado.
     * @param prompt mensaje a mostrar al usuario
     * @return valor entero leído del usuario
     */
    private int readInt(String prompt) {
        while (true) {
            String l = readLine(prompt);
            try {
                int v = Integer.parseInt(l);
                if (v < 0) {
                    System.out.println("Value must be >= 0");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }
}