package com.diegoehg.onlinestore.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

/**
 * Data Transfer Object for Product
 */
public class ProductDTO {
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;
    
    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description must be less than 2000 characters")
    private String description;
    
    @NotNull(message = "Images list cannot be null")
    private List<String> images = new ArrayList<>();
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;
    
    @NotNull(message = "Seller information is required")
    @Valid
    private SellerDTO seller;

    // Default constructor
    public ProductDTO() {
    }

    // Constructor with fields
    public ProductDTO(Long id, String title, String description, List<String> images, BigDecimal price, SellerDTO seller) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.images = images;
        this.price = price;
        this.seller = seller;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public SellerDTO getSeller() {
        return seller;
    }

    public void setSeller(SellerDTO seller) {
        this.seller = seller;
    }
}