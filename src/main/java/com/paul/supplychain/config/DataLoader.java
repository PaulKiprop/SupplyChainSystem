package com.paul.supplychain.config;

import com.paul.supplychain.entity.*;
import com.paul.supplychain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.*;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DataLoader {

    private final ProductRepository productRepo;
    private final SupplierRepository supplierRepo;
    private final WarehouseRepository warehouseRepo;
    private final InventoryRepository inventoryRepo;

    @Bean
    CommandLineRunner loadData() {
        return args -> {
            Random random = new Random(42);
            List<Product> products;
            List<Warehouse> warehouses;

            if (productRepo.count() == 0) {
                products = new ArrayList<>();
                for (int i = 1; i <= 100; i++) {
                    Product p = new Product();
                    p.setName("Product " + i);
                    p.setSku("SKU-" + i);
                    p.setDescription("Description for product " + i);
                    p.setPrice(50 + random.nextDouble() * 500);
                    products.add(p);
                }
                products = productRepo.saveAll(products);
            } else {
                products = productRepo.findAll();
            }

            if (supplierRepo.count() == 0) {
                List<Supplier> suppliers = new ArrayList<>();
                for (int i = 1; i <= 30; i++) {
                    Supplier s = new Supplier();
                    s.setName("Supplier " + i);
                    s.setEmail("supplier" + i + "@mail.com");

                    int productCount = Math.min(products.size(), 3 + (i % 5));
                    int startIndex = ((i - 1) * 7) % products.size();
                    List<Product> assignedProducts = new ArrayList<>();
                    for (int j = 0; j < productCount; j++) {
                        assignedProducts.add(products.get((startIndex + j) % products.size()));
                    }
                    s.setProducts(assignedProducts);
                    suppliers.add(s);
                }
                supplierRepo.saveAll(suppliers);
            }

            if (warehouseRepo.count() == 0) {
                warehouses = new ArrayList<>();
                for (int i = 1; i <= 5; i++) {
                    Warehouse w = new Warehouse();
                    w.setName("Warehouse " + i);
                    w.setLocation("City " + i);
                    warehouses.add(w);
                }
                warehouses = warehouseRepo.saveAll(warehouses);
            } else {
                warehouses = warehouseRepo.findAll();
            }

            if (inventoryRepo.count() == 0) {
                List<Inventory> inventoryList = new ArrayList<>();
                for (int warehouseIndex = 0; warehouseIndex < warehouses.size(); warehouseIndex++) {
                    Warehouse warehouse = warehouses.get(warehouseIndex);
                    for (int productIndex = 0; productIndex < products.size(); productIndex++) {
                        Product product = products.get(productIndex);

                        Inventory inv = new Inventory();
                        inv.setWarehouse(warehouse);
                        inv.setProduct(product);
                        inv.setQuantity(25 + ((warehouseIndex * 31 + productIndex * 17) % 476));
                        inventoryList.add(inv);
                    }
                }
                inventoryRepo.saveAll(inventoryList);
            }

            System.out.println("Sample data loaded!");
        };
    }
}
