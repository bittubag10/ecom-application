package com.app.ecom.Transformer;

import com.app.ecom.DTO.Request.ProductRequest;
import com.app.ecom.DTO.Response.ProductResponse;
import com.app.ecom.Model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductTransformer {

    // Request DTO se Entity banane ke liye
    public Product toEntity(ProductRequest request) {
        if (request == null) return null;

        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .category(request.getCategory())
                .imageUrl(request.getImageUrl())
                .build();
    }

    // Entity se Response DTO banane ke liye
    public ProductResponse toResponse(Product product) {
        if (product == null) return null;

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .active(product.isActive())
                .build();
    }


    public void updateEntityFromRequest(Product existingProduct, ProductRequest request) {
        if (request == null) return;

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStockQuantity(request.getStockQuantity());
        existingProduct.setCategory(request.getCategory());
        existingProduct.setImageUrl(request.getImageUrl());
        // Agar active status bhi update karwana hai to:
        // existingProduct.setActive(request.isActive());
    }
}