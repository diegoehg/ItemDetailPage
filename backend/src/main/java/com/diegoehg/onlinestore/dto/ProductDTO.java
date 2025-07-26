package com.diegoehg.onlinestore.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for Product
 */
public class ProductDTO {
    private Long id;
    private String title;
    private String description;
    private List<String> images = new ArrayList<>();
    private BigDecimal price;
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