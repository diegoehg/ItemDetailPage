package com.diegoehg.onlinestore.dto;

/**
 * Data Transfer Object for PaymentMethod
 */
public class PaymentMethodDTO {
    private Long id;
    private String name;

    // Default constructor
    public PaymentMethodDTO() {
    }

    // Constructor with fields
    public PaymentMethodDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}