package com.diegoehg.onlinestore.service;

import com.diegoehg.onlinestore.dto.SellerDTO;

import java.util.List;

/**
 * Interface for seller service operations
 */
public interface SellerService {

    /**
     * Retrieves all sellers
     * @return List of all sellers as DTOs
     */
    List<SellerDTO> getAllSellers();
}