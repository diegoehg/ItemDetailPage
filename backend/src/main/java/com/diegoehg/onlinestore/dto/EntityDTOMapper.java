package com.diegoehg.onlinestore.dto;

import com.diegoehg.onlinestore.model.PaymentMethod;
import com.diegoehg.onlinestore.model.Product;
import com.diegoehg.onlinestore.model.Seller;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class to convert between entities and DTOs
 */
public class EntityDTOMapper {

    /**
     * Converts a Product entity to a ProductDTO
     * @param product The Product entity to convert
     * @return The corresponding ProductDTO
     */
    public static ProductDTO toProductDTO(Product product) {
        if (product == null) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setTitle(product.getTitle());
        productDTO.setDescription(product.getDescription());
        productDTO.setImages(product.getImages());
        productDTO.setPrice(product.getPrice());
        
        if (product.getSeller() != null) {
            productDTO.setSeller(toSellerDTO(product.getSeller()));
        }
        
        return productDTO;
    }

    /**
     * Converts a list of Product entities to a list of ProductDTOs
     * @param products The list of Product entities to convert
     * @return The corresponding list of ProductDTOs
     */
    public static List<ProductDTO> toProductDTOList(List<Product> products) {
        if (products == null) {
            return List.of();
        }
        
        return products.stream()
                .map(EntityDTOMapper::toProductDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a Seller entity to a SellerDTO
     * @param seller The Seller entity to convert
     * @return The corresponding SellerDTO
     */
    public static SellerDTO toSellerDTO(Seller seller) {
        if (seller == null) {
            return null;
        }

        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setId(seller.getId());
        sellerDTO.setName(seller.getName());
        
        if (seller.getPaymentMethods() != null) {
            List<PaymentMethodDTO> paymentMethodDTOs = seller.getPaymentMethods().stream()
                    .map(EntityDTOMapper::toPaymentMethodDTO)
                    .collect(Collectors.toList());
            sellerDTO.setPaymentMethods(paymentMethodDTOs);
        }
        
        return sellerDTO;
    }

    /**
     * Converts a PaymentMethod entity to a PaymentMethodDTO
     * @param paymentMethod The PaymentMethod entity to convert
     * @return The corresponding PaymentMethodDTO
     */
    public static PaymentMethodDTO toPaymentMethodDTO(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }

        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
        paymentMethodDTO.setId(paymentMethod.getId());
        paymentMethodDTO.setName(paymentMethod.getName());
        
        return paymentMethodDTO;
    }

    /**
     * Converts a ProductDTO to a Product entity
     * @param productDTO The ProductDTO to convert
     * @return The corresponding Product entity
     */
    public static Product toProduct(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }

        Product product = new Product();
        product.setId(productDTO.getId());
        product.setTitle(productDTO.getTitle());
        product.setDescription(productDTO.getDescription());
        product.setImages(productDTO.getImages());
        product.setPrice(productDTO.getPrice());
        
        if (productDTO.getSeller() != null) {
            product.setSeller(toSeller(productDTO.getSeller()));
        }
        
        return product;
    }

    /**
     * Converts a SellerDTO to a Seller entity
     * @param sellerDTO The SellerDTO to convert
     * @return The corresponding Seller entity
     */
    public static Seller toSeller(SellerDTO sellerDTO) {
        if (sellerDTO == null) {
            return null;
        }

        Seller seller = new Seller();
        seller.setId(sellerDTO.getId());
        seller.setName(sellerDTO.getName());
        
        return seller;
    }
}