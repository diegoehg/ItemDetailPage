package com.diegoehg.onlinestore.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for Seller
 */
public class SellerDTO {
    private Long id;
    
    private String name;
    
    private List<PaymentMethodDTO> paymentMethods = new ArrayList<>();

    // Default constructor
    public SellerDTO() {
    }

    // Constructor with fields
    public SellerDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Constructor with all fields
    public SellerDTO(Long id, String name, List<PaymentMethodDTO> paymentMethods) {
        this.id = id;
        this.name = name;
        this.paymentMethods = paymentMethods;
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

    public List<PaymentMethodDTO> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethodDTO> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
}