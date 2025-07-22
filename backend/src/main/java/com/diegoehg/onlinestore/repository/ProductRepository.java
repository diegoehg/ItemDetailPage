package com.diegoehg.onlinestore.repository;

import com.diegoehg.onlinestore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring Data JPA will automatically implement basic CRUD operations
    // Custom query methods can be added here if needed
}