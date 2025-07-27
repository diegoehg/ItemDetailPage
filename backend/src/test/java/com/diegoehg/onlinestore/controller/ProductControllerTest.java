package com.diegoehg.onlinestore.controller;

import com.diegoehg.onlinestore.dto.ProductDTO;
import com.diegoehg.onlinestore.exception.ResourceNotFoundException;
import com.diegoehg.onlinestore.model.PagedResponse;
import com.diegoehg.onlinestore.model.ResponseStatus;
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

    private ProductDTO product1;
    private ProductDTO product2;
    private List<ProductDTO> productList;

    @BeforeEach
    void setUp() {
        product1 = new ProductDTO();
        product1.setId(1L);
        product1.setTitle("Product 1");
        product1.setDescription("Description for product 1");
        product1.setImages(Arrays.asList("image1.jpg", "image2.jpg"));
        product1.setPrice(new BigDecimal("99.99"));

        product2 = new ProductDTO();
        product2.setId(2L);
        product2.setTitle("Product 2");
        product2.setDescription("Description for product 2");
        product2.setImages(Arrays.asList("image3.jpg", "image4.jpg"));
        product2.setPrice(new BigDecimal("149.99"));

        productList = Arrays.asList(product1, product2);
    }

    @Test
    void getAllProducts_defaultCase() throws Exception {
        PagedResponse<ProductDTO> pagedResponse = new PagedResponse<>(
                productList,
                1,
                10,
                2,
                1,
                true,
                true
        );

        when(productService.getProductsPaginated(1, 10)).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(ResponseStatus.SUCCESS.toString())))
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.page", is(1)))
                .andExpect(jsonPath("$.data.size", is(10)))
                .andExpect(jsonPath("$.data.totalElements", is(2)))
                .andExpect(jsonPath("$.data.totalPages", is(1)))
                .andExpect(jsonPath("$.data.last", is(true)))
                .andExpect(jsonPath("$.data.first", is(true)))
                .andExpect(jsonPath("$.data.content[0].id", is(1)))
                .andExpect(jsonPath("$.data.content[0].title", is("Product 1")))
                .andExpect(jsonPath("$.data.content[1].id", is(2)))
                .andExpect(jsonPath("$.data.content[1].title", is("Product 2")));
    }
    
    @Test
    void getAllProducts_getPageWithSize() throws Exception {
        PagedResponse<ProductDTO> pagedResponse = new PagedResponse<>(
            productList,
            6,
            10,
            2,
            1,
            true,
            true
        );
        
        when(productService.getProductsPaginated(6, 10)).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/products?page=6&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(ResponseStatus.SUCCESS.toString())))
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.page", is(6)))
                .andExpect(jsonPath("$.data.size", is(10)))
                .andExpect(jsonPath("$.data.totalElements", is(2)))
                .andExpect(jsonPath("$.data.totalPages", is(1)))
                .andExpect(jsonPath("$.data.last", is(true)))
                .andExpect(jsonPath("$.data.first", is(true)))
                .andExpect(jsonPath("$.data.content[0].id", is(1)))
                .andExpect(jsonPath("$.data.content[0].title", is("Product 1")))
                .andExpect(jsonPath("$.data.content[1].id", is(2)))
                .andExpect(jsonPath("$.data.content[1].title", is("Product 2")));
    }

    @Test
    void getProductById_whenProductExists() throws Exception {
        when(productService.getProductById(1L)).thenReturn(product1);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(ResponseStatus.SUCCESS.toString())))
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.title", is("Product 1")));
    }

    @Test
    void getProductById_whenProductDoesNotExist() throws Exception {
        ResourceNotFoundException exception = new ResourceNotFoundException("Product", "3");

        when(productService.getProductById(3L)).thenThrow(exception);

        mockMvc.perform(get("/api/products/3"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(ResponseStatus.ERROR.toString())))
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is(exception.getMessage())));
    }

    @Test
    void createProduct() throws Exception {
        ProductDTO newProduct = new ProductDTO();
        newProduct.setTitle("New Product");
        newProduct.setDescription("Description for new product");
        newProduct.setImages(Arrays.asList("new-image1.jpg", "new-image2.jpg"));
        newProduct.setPrice(new BigDecimal("199.99"));

        ProductDTO savedProduct = new ProductDTO();
        savedProduct.setId(3L);
        savedProduct.setTitle("New Product");
        savedProduct.setDescription("Description for new product");
        savedProduct.setImages(Arrays.asList("new-image1.jpg", "new-image2.jpg"));
        savedProduct.setPrice(new BigDecimal("199.99"));

        when(productService.saveProduct(any(ProductDTO.class))).thenReturn(savedProduct);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(ResponseStatus.SUCCESS.toString())))
                .andExpect(jsonPath("$.code", is(201)))
                .andExpect(jsonPath("$.data.id", is(3)))
                .andExpect(jsonPath("$.data.title", is("New Product")));
    }

    @Test
    void updateProduct_whenProductExists() throws Exception {
        ProductDTO updatedProduct = new ProductDTO();
        updatedProduct.setTitle("Updated Product 1");
        updatedProduct.setDescription("Updated description for product 1");
        updatedProduct.setImages(Arrays.asList("updated-image1.jpg", "updated-image2.jpg"));
        updatedProduct.setPrice(new BigDecimal("129.99"));

        ProductDTO savedUpdatedProduct = new ProductDTO();
        savedUpdatedProduct.setId(1L);
        savedUpdatedProduct.setTitle("Updated Product 1");
        savedUpdatedProduct.setDescription("Updated description for product 1");
        savedUpdatedProduct.setImages(Arrays.asList("updated-image1.jpg", "updated-image2.jpg"));
        savedUpdatedProduct.setPrice(new BigDecimal("129.99"));

        when(productService.updateProduct(eq(1L), any(ProductDTO.class))).thenReturn(savedUpdatedProduct);

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(ResponseStatus.SUCCESS.toString())))
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.title", is("Updated Product 1")));
    }

    @Test
    void updateProduct_whenProductDoesNotExist() throws Exception {
        ResourceNotFoundException exception = new ResourceNotFoundException("Product", "3");

        ProductDTO updatedProduct = new ProductDTO();
        updatedProduct.setTitle("Updated Product 1");
        updatedProduct.setDescription("Updated description for product 1");
        updatedProduct.setImages(Arrays.asList("updated-image1.jpg", "updated-image2.jpg"));
        updatedProduct.setPrice(new BigDecimal("129.99"));

        when(productService.updateProduct(eq(3L), any(ProductDTO.class))).thenThrow(exception);

        mockMvc.perform(put("/api/products/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(ResponseStatus.ERROR.toString())))
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is(exception.getMessage())));
    }

    @Test
    void deleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(ResponseStatus.SUCCESS.toString())))
                .andExpect(jsonPath("$.code", is(204)));
    }

    @Test
    void deleteProduct_whenProductDoesNotExist() throws Exception {
        ResourceNotFoundException exception = new ResourceNotFoundException("Product", "3");

        doThrow(exception).when(productService).deleteProduct(3L);

        mockMvc.perform(delete("/api/products/3"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(ResponseStatus.ERROR.toString())))
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is(exception.getMessage())));
    }
}