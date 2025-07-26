package com.diegoehg.onlinestore.service;

import com.diegoehg.onlinestore.dto.EntityDTOMapper;
import com.diegoehg.onlinestore.dto.ProductDTO;
import com.diegoehg.onlinestore.exception.ResourceNotFoundException;
import com.diegoehg.onlinestore.model.Product;
import com.diegoehg.onlinestore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the ProductService interface
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return EntityDTOMapper.toProductDTOList(products);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", String.valueOf(id)));
        return EntityDTOMapper.toProductDTO(product);
    }

    @Override
    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product product = EntityDTOMapper.toProduct(productDTO);
        Product savedProduct = productRepository.save(product);
        return EntityDTOMapper.toProductDTO(savedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", String.valueOf(id)));
        productRepository.deleteById(id);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", String.valueOf(id)));

        product.setTitle(productDTO.getTitle());
        product.setDescription(productDTO.getDescription());
        product.setImages(productDTO.getImages());
        product.setPrice(productDTO.getPrice());
        
        if (productDTO.getSeller() != null) {
            product.setSeller(EntityDTOMapper.toSeller(productDTO.getSeller()));
        }

        Product updatedProduct = productRepository.save(product);
        return EntityDTOMapper.toProductDTO(updatedProduct);
    }
}