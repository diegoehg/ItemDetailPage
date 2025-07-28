package com.diegoehg.onlinestore.controller;

import com.diegoehg.onlinestore.dto.SellerDTO;
import com.diegoehg.onlinestore.model.Response;
import com.diegoehg.onlinestore.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
@CrossOrigin(origins = "*") // Allow requests from any origin for development
public class SellerController {

    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping
    public ResponseEntity<Response<List<SellerDTO>>> getAllSellers() {
        List<SellerDTO> sellers = sellerService.getAllSellers();
        return ResponseEntity.ok(Response.success(sellers, HttpStatus.OK.value()));
    }
}