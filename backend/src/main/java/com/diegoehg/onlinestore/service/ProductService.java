package com.diegoehg.onlinestore.service;

import com.diegoehg.onlinestore.dto.ProductDTO;
import com.diegoehg.onlinestore.exception.ResourceNotFoundException;
import com.diegoehg.onlinestore.model.PagedResponse;

import java.util.List;

/**
 * Interface for product service operations
 */
public interface ProductService {

    /**
     * Retrieves all products
     * @return List of all products as DTOs
     */
    List<ProductDTO> getAllProducts();
    
    /**
     * Retrieves products with pagination
     * @param page Page number (1-based)
     * @param size Page size
     * @return Paginated response with products
     */
    PagedResponse<ProductDTO> getProductsPaginated(int page, int size);

    /**
     * Retrieves a product by its ID
     * @param id The ID of the product to retrieve
     * @return The product as DTO if found
     * @throws ResourceNotFoundException if the product is not found
     */
    ProductDTO getProductById(Long id);

    /**
     * Saves a new product
     * @param productDTO The product DTO to save
     * @return The saved product as DTO with generated ID
     */
    ProductDTO saveProduct(ProductDTO productDTO);

    /**
     * Deletes a product by its ID
     * @param id The ID of the product to delete
     */
    void deleteProduct(Long id);

    /**
     * Updates an existing product
     * @param id The ID of the product to update
     * @param productDTO The updated product details as DTO
     * @return The updated product as DTO
     * @throws RuntimeException if the product is not found
     */
    ProductDTO updateProduct(Long id, ProductDTO productDTO);
}