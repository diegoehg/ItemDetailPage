package com.diegoehg.onlinestore.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testProductCreation() {
        // Arrange
        String title = "Test Product";
        String description = "This is a test product description";
        List<String> images = Arrays.asList("image1.jpg", "image2.jpg");
        BigDecimal price = new BigDecimal("99.99");

        // Act
        Product product = new Product(title, description, images, price);

        // Assert
        assertEquals(title, product.getTitle());
        assertEquals(description, product.getDescription());
        assertEquals(images, product.getImages());
        assertEquals(price, product.getPrice());
        assertNull(product.getId()); // ID should be null until saved to database
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        Product product = new Product();
        String title = "Updated Title";
        String description = "Updated description";
        List<String> images = Arrays.asList("new-image1.jpg", "new-image2.jpg");
        BigDecimal price = new BigDecimal("149.99");
        Long id = 1L;

        // Act
        product.setId(id);
        product.setTitle(title);
        product.setDescription(description);
        product.setImages(images);
        product.setPrice(price);

        // Assert
        assertEquals(id, product.getId());
        assertEquals(title, product.getTitle());
        assertEquals(description, product.getDescription());
        assertEquals(images, product.getImages());
        assertEquals(price, product.getPrice());
    }

    @Test
    void testToString() {
        // Arrange
        Product product = new Product(
                "Test Product",
                "Test Description",
                Arrays.asList("image1.jpg", "image2.jpg"),
                new BigDecimal("99.99")
        );
        product.setId(1L);

        // Act
        String toString = product.toString();

        // Assert
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("title='Test Product'"));
        assertTrue(toString.contains("description='Test Description'"));
        assertTrue(toString.contains("images=[image1.jpg, image2.jpg]"));
        assertTrue(toString.contains("price=99.99"));
    }
}