package com.diegoehg.onlinestore.service;

import com.diegoehg.onlinestore.dto.EntityDTOMapper;
import com.diegoehg.onlinestore.dto.SellerDTO;
import com.diegoehg.onlinestore.model.Seller;
import com.diegoehg.onlinestore.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the SellerService interface
 */
@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Override
    public List<SellerDTO> getAllSellers() {
        List<Seller> sellers = sellerRepository.findAll();
        return sellers.stream()
                .map(EntityDTOMapper::toSellerDTO)
                .collect(Collectors.toList());
    }
}