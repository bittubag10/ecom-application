package com.app.ecom.Service;

import com.app.ecom.DTO.Request.ProductRequest;
import com.app.ecom.DTO.Response.ProductResponse;
import com.app.ecom.Model.Product;
import com.app.ecom.Repository.ProductRepository;
import com.app.ecom.Transformer.ProductTransformer; // Transformer Import
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductTransformer productTransformer; // Inject Transformer

    public ProductResponse createProduct(ProductRequest productRequest) {

        Product product = productTransformer.toEntity(productRequest);

        Product savedProduct = productRepository.save(product);

        return productTransformer.toResponse(savedProduct);
    }


    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        productTransformer.updateEntityFromRequest(existingProduct, productRequest);

        Product updatedProduct = productRepository.save(existingProduct);

        return productTransformer.toResponse(updatedProduct);
    }


    public void deleteProductPermanent(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    // Product ko inactive kar do delete karne ki jagah
    @Transactional
    public void softDeleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));


        product.setActive(false);
        productRepository.save(product);
    }

    public ProductResponse getProduct(Long id) {
        Product product=productRepository.findById(id).
                orElseThrow(()->new RuntimeException("Product Not found !!! "+id));
        return productTransformer.toResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(productTransformer::toResponse)
                .collect(Collectors.toList());

    }

    public List<ProductResponse> searchProducts(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }

        List<Product> products = productRepository.searchProducts(keyword);

        return products.stream()
                .map(productTransformer::toResponse)
                .collect(Collectors.toList());
    }

}