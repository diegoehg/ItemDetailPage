package com.diegoehg.onlinestore.config;

import com.diegoehg.onlinestore.model.PaymentMethod;
import com.diegoehg.onlinestore.model.Product;
import com.diegoehg.onlinestore.model.Seller;
import com.diegoehg.onlinestore.repository.PaymentMethodRepository;
import com.diegoehg.onlinestore.repository.ProductRepository;
import com.diegoehg.onlinestore.repository.SellerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

/**
 * Configuration class to initialize the database with sample data.
 * This initializer populates the following tables:
 * - sellers
 * - payment_methods
 * - seller_payment_methods (join table)
 * - products
 * - product_images
 */
@Configuration
public class DataInitializer {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
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
            PaymentMethod creditCard = new PaymentMethod("Credit Card");
            PaymentMethod debitCard = new PaymentMethod("Debit Card");
            PaymentMethod paypal = new PaymentMethod("PayPal");
            PaymentMethod bankTransfer = new PaymentMethod("Bank Transfer");
            
            List<PaymentMethod> paymentMethods = paymentMethodRepository.saveAll(
                    List.of(creditCard, debitCard, paypal, bankTransfer)
            );
            
            // Initialize sellers
            logger.info("Initializing sellers...");
            Seller techStore = new Seller("Tech Store");
            Seller fashionOutlet = new Seller("Fashion Outlet");
            Seller homeGoods = new Seller("Home Goods");
            
            // Assign payment methods to sellers
            techStore.addPaymentMethod(creditCard);
            techStore.addPaymentMethod(debitCard);
            techStore.addPaymentMethod(paypal);
            
            fashionOutlet.addPaymentMethod(creditCard);
            fashionOutlet.addPaymentMethod(paypal);
            
            homeGoods.addPaymentMethod(creditCard);
            homeGoods.addPaymentMethod(debitCard);
            homeGoods.addPaymentMethod(bankTransfer);
            
            sellerRepository.saveAll(
                    List.of(techStore, fashionOutlet, homeGoods)
            );
            
            // Initialize products
            logger.info("Initializing products...");
            
            // Tech Store products
            Product laptop = new Product(
                    "High-Performance Laptop",
                    "15.6-inch laptop with the latest processor, 16GB RAM, and 512GB SSD storage.",
                    List.of(
                            "https://example.com/images/laptop1.jpg",
                            "https://example.com/images/laptop2.jpg",
                            "https://example.com/images/laptop3.jpg"
                    ),
                    new BigDecimal("1299.99"),
                    techStore
            );
            
            Product smartphone = new Product(
                    "Premium Smartphone",
                    "6.5-inch smartphone with 128GB storage, 8GB RAM, and a high-resolution camera.",
                    List.of(
                            "https://example.com/images/phone1.jpg",
                            "https://example.com/images/phone2.jpg"
                    ),
                    new BigDecimal("899.99"),
                    techStore
            );
            
            // Fashion Outlet products
            Product jeans = new Product(
                    "Designer Jeans",
                    "Premium quality jeans with a modern fit and durable fabric.",
                    List.of(
                            "https://example.com/images/jeans1.jpg",
                            "https://example.com/images/jeans2.jpg"
                    ),
                    new BigDecimal("79.99"),
                    fashionOutlet
            );
            
            Product tshirt = new Product(
                    "Casual T-Shirt",
                    "Comfortable cotton t-shirt available in various colors.",
                    List.of(
                            "https://example.com/images/tshirt1.jpg",
                            "https://example.com/images/tshirt2.jpg",
                            "https://example.com/images/tshirt3.jpg"
                    ),
                    new BigDecimal("24.99"),
                    fashionOutlet
            );
            
            // Home Goods products
            Product coffeeTable = new Product(
                    "Modern Coffee Table",
                    "Elegant coffee table with a glass top and wooden legs.",
                    List.of(
                            "https://example.com/images/table1.jpg",
                            "https://example.com/images/table2.jpg"
                    ),
                    new BigDecimal("249.99"),
                    homeGoods
            );
            
            Product bedSet = new Product(
                    "Luxury Bed Set",
                    "King-size bed set including mattress, frame, and headboard.",
                    List.of(
                            "https://example.com/images/bedset1.jpg",
                            "https://example.com/images/bedset2.jpg",
                            "https://example.com/images/bedset3.jpg"
                    ),
                    new BigDecimal("1499.99"),
                    homeGoods
            );
            
            productRepository.saveAll(
                    List.of(laptop, smartphone, jeans, tshirt, coffeeTable, bedSet)
            );
            
            logger.info("Database initialization completed successfully!");
        };
    }
}