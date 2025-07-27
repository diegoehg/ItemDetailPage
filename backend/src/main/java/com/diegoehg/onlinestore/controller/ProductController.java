package com.diegoehg.onlinestore.controller;

import com.diegoehg.onlinestore.dto.ProductDTO;
import com.diegoehg.onlinestore.model.Response;
import com.diegoehg.onlinestore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*") // Allow requests from any origin for development
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Response<?>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        
        return ResponseEntity.ok(Response.success(
            productService.getProductsPaginated(page, size), 
            HttpStatus.OK.value())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ProductDTO>> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(Response.success(product, HttpStatus.OK.value()));
    }

    @PostMapping
    public ResponseEntity<Response<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO savedProduct = productService.saveProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success(savedProduct, HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<ProductDTO>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(Response.success(updatedProduct, HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Response.success(null, HttpStatus.NO_CONTENT.value()));
    }
}