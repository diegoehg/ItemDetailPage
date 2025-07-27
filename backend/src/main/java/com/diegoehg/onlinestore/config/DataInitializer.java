package com.diegoehg.onlinestore.config;

import com.diegoehg.onlinestore.model.PaymentMethod;
import com.diegoehg.onlinestore.model.Product;
import com.diegoehg.onlinestore.model.Seller;
import com.diegoehg.onlinestore.repository.PaymentMethodRepository;
import com.diegoehg.onlinestore.repository.ProductRepository;
import com.diegoehg.onlinestore.repository.SellerRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration class to initialize the database with sample data from CSV files.
 * This initializer populates the following tables:
 * - payment_methods (initialized with 4 fixed payment methods)
 * - sellers (loaded from sellers.csv)
 * - seller_payment_methods (join table, relationships defined in sellers.csv)
 * - products (loaded from products.csv, which contains 300 products)
 * - product_images (image URLs are defined in products.csv)
 * 
 * The CSV files are located in the resources/data directory:
 * - sellers.csv: Contains seller names and their associated payment method IDs
 * - products.csv: Contains 300 product details including title, description, price, seller ID, and image URLs
 */
@Configuration
public class DataInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private static final String SELLERS_CSV = "data/sellers.csv";
    private static final String PRODUCTS_CSV = "data/products.csv";

    /**
     * Creates a CommandLineRunner bean that initializes the database with sample data.
     * This will run automatically when the application starts.
     */
    @Bean
    public CommandLineRunner initDatabase(
            SellerRepository sellerRepository,
            PaymentMethodRepository paymentMethodRepository,
            ProductRepository productRepository) {

        return args -> {
            logger.info("Starting database initialization...");

            // Check if data already exists to avoid duplicate initialization
            if (sellerRepository.count() > 0) {
                logger.info("Database already contains data. Skipping initialization.");
                return;
            }

            // Initialize payment methods
            logger.info("Initializing payment methods...");
            PaymentMethod creditCard = new PaymentMethod("VISA Credit Card");
            PaymentMethod debitCard = new PaymentMethod("MasterCard Debit Card");
            PaymentMethod paypal = new PaymentMethod("PayPal");
            PaymentMethod bankTransfer = new PaymentMethod("Bank Transfer");

            List<PaymentMethod> paymentMethods = paymentMethodRepository.saveAll(
                    List.of(creditCard, debitCard, paypal, bankTransfer)
            );

            // Create a map of payment methods by ID for easy lookup
            Map<Long, PaymentMethod> paymentMethodMap = new HashMap<>();
            paymentMethodMap.put(1L, creditCard);
            paymentMethodMap.put(2L, debitCard);
            paymentMethodMap.put(3L, paypal);
            paymentMethodMap.put(4L, bankTransfer);

            // Initialize sellers from CSV
            logger.info("Initializing sellers from CSV...");
            List<Seller> sellers = loadSellersFromCsv(paymentMethodMap);
            sellerRepository.saveAll(sellers);

            // Create a map of sellers by ID for easy lookup
            Map<Long, Seller> sellerMap = new HashMap<>();
            long sellerId = 1;
            for (Seller seller : sellers) {
                sellerMap.put(sellerId++, seller);
            }

            // Initialize products from CSV
            logger.info("Initializing products from CSV...");
            List<Product> products = loadProductsFromCsv(sellerMap);
            productRepository.saveAll(products);

            logger.info("Database initialization completed successfully with {} sellers and {} products!",
                    sellers.size(), products.size());
        };
    }

    /**
     * Loads sellers from the CSV file and assigns payment methods.
     *
     * @param paymentMethodMap Map of payment methods by ID
     * @return List of sellers
     */
    private List<Seller> loadSellersFromCsv(Map<Long, PaymentMethod> paymentMethodMap) {
        List<Seller> sellers = new ArrayList<>();

        try {
            Resource resource = new ClassPathResource(SELLERS_CSV);
            try (Reader reader = new BufferedReader(new InputStreamReader(
                    resource.getInputStream(), StandardCharsets.UTF_8))) {

                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .setIgnoreHeaderCase(true)
                        .setTrim(true)
                        .build());

                for (CSVRecord record : csvParser) {
                    String name = record.get("name");
                    Seller seller = new Seller(name);

                    // Get payment method IDs (they start from column index 1)
                    String[] paymentMethodIds = record.get("payment_method_ids").split(",");
                    for (String idStr : paymentMethodIds) {
                        Long id = Long.parseLong(idStr.trim());
                        PaymentMethod paymentMethod = paymentMethodMap.get(id);
                        if (paymentMethod != null) {
                            seller.addPaymentMethod(paymentMethod);
                        }
                    }

                    sellers.add(seller);
                }
            }
        } catch (IOException e) {
            logger.error("Error loading sellers from CSV", e);
        }

        return sellers;
    }

    /**
     * Loads products from the CSV file.
     *
     * @param sellerMap Map of sellers by ID
     * @return List of products
     */
    private List<Product> loadProductsFromCsv(Map<Long, Seller> sellerMap) {
        List<Product> products = new ArrayList<>();

        try {
            Resource resource = new ClassPathResource(PRODUCTS_CSV);
            try (Reader reader = new BufferedReader(new InputStreamReader(
                    resource.getInputStream(), StandardCharsets.UTF_8))) {

                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .setIgnoreHeaderCase(true)
                        .setTrim(true)
                        .build());

                for (CSVRecord record : csvParser) {
                    String description = record.get("description");
                    BigDecimal price = new BigDecimal(record.get("price"));
                    Long sellerId = Long.parseLong(record.get("seller_id"));
                    String[] imageUrls = record.get("image_urls").split(";");
                    String title = record.get(0);

                    Seller seller = sellerMap.get(sellerId);
                    if (seller != null) {
                        Product product = new Product(
                                title,
                                description,
                                Arrays.asList(imageUrls),
                                price,
                                seller
                        );
                        products.add(product);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error loading products from CSV", e);
        }

        return products;
    }

}