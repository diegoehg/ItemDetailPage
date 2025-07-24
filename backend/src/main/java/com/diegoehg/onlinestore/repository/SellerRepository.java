package com.diegoehg.onlinestore.repository;

import com.diegoehg.onlinestore.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    // Spring Data JPA will automatically implement basic CRUD operations
    // Custom query methods can be added here if needed
}