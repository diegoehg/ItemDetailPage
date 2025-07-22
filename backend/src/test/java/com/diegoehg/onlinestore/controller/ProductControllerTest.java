package com.diegoehg.onlinestore.controller;

import com.diegoehg.onlinestore.model.Product;
import com.diegoehg.onlinestore.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product1;
    private Product product2;
    private List<Product> productList;

    @BeforeEach
    void setUp() {
        product1 = new Product(
                "Product 1",
                "Description for product 1",
                Arrays.asList("image1.jpg", "image2.jpg"),
                new BigDecimal("99.99")
        );
        product1.setId(1L);

        product2 = new Product(
                "Product 2",
                "Description for product 2",
                Arrays.asList("image3.jpg", "image4.jpg"),
                new BigDecimal("149.99")
        );
        product2.setId(2L);

        productList = Arrays.asList(product1, product2);
    }

    @Test
    void getAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Product 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Product 2")));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getProductById() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.of(product1));
        when(productService.getProductById(3L)).thenReturn(Optional.empty());

        // Test successful retrieval
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Product 1")));

        // Test product not found
        mockMvc.perform(get("/api/products/3"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(1L);
        verify(productService, times(1)).getProductById(3L);
    }

    @Test
    void createProduct() throws Exception {
        Product newProduct = new Product(
                "New Product",
                "Description for new product",
                Arrays.asList("new-image1.jpg", "new-image2.jpg"),
                new BigDecimal("199.99")
        );

        Product savedProduct = new Product(
                "New Product",
                "Description for new product",
                Arrays.asList("new-image1.jpg", "new-image2.jpg"),
                new BigDecimal("199.99")
        );
        savedProduct.setId(3L);

        when(productService.saveProduct(any(Product.class))).thenReturn(savedProduct);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.title", is("New Product")));

        verify(productService, times(1)).saveProduct(any(Product.class));
    }

    @Test
    void updateProduct() throws Exception {
        Product updatedProduct = new Product(
                "Updated Product 1",
                "Updated description for product 1",
                Arrays.asList("updated-image1.jpg", "updated-image2.jpg"),
                new BigDecimal("129.99")
        );

        Product savedUpdatedProduct = new Product(
                "Updated Product 1",
                "Updated description for product 1",
                Arrays.asList("updated-image1.jpg", "updated-image2.jpg"),
                new BigDecimal("129.99")
        );
        savedUpdatedProduct.setId(1L);

        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(savedUpdatedProduct);
        when(productService.updateProduct(eq(3L), any(Product.class))).thenThrow(new RuntimeException("Product not found"));

        // Test successful update
        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Updated Product 1")));

        // Test product not found
        mockMvc.perform(put("/api/products/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
        verify(productService, times(1)).updateProduct(eq(3L), any(Product.class));
    }

    @Test
    void deleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }
}