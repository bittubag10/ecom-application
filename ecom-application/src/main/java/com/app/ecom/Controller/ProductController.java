package com.app.ecom.Controller;

import com.app.ecom.DTO.Request.ProductRequest;
import com.app.ecom.DTO.Response.ProductResponse;
import com.app.ecom.Model.Product;
import com.app.ecom.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest){

        return new ResponseEntity<ProductResponse>
                (productService.createProduct(productRequest), HttpStatus.CREATED);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest productRequest) {

        ProductResponse response = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProductPermanent(id);
        return ResponseEntity.ok("Product deleted successfully with ID: " + id);
    }

    // Product ko inactive kar do delete karne ki jagah
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        productService.softDeleteProduct(id);
        return ResponseEntity.ok("Product deactivated successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id){
        ProductResponse response=productService.getProduct(id);
        return ResponseEntity.ok(response);

    }
    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAll(){
        List<ProductResponse>productResponses=productService.getAllProducts();
        return new ResponseEntity<>(productResponses,HttpStatus.OK);
    }
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam(name = "keyword") String keyword) {

        List<ProductResponse> results = productService.searchProducts(keyword);
        return ResponseEntity.ok(results);
    }

}
