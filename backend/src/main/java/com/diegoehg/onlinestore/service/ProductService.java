package com.diegoehg.onlinestore.service;

import com.diegoehg.onlinestore.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Interface for product service operations
 */
public interface ProductService {

    /**
     * Retrieves all products
     * @return List of all products
     */
    List<Product> getAllProducts();

    /**
     * Retrieves a product by its ID
     * @param id The ID of the product to retrieve
     * @return Optional containing the product if found, empty otherwise
     */
    Optional<Product> getProductById(Long id);

    /**
     * Saves a new product
     * @param product The product to save
     * @return The saved product with generated ID
     */
    Product saveProduct(Product product);

    /**
     * Deletes a product by its ID
     * @param id The ID of the product to delete
     */
    void deleteProduct(Long id);

    /**
     * Updates an existing product
     * @param id The ID of the product to update
     * @param productDetails The updated product details
     * @return The updated product
     * @throws RuntimeException if the product is not found
     */
    Product updateProduct(Long id, Product productDetails);
}