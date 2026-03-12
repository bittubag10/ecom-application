package com.app.ecom.Controller;

import com.app.ecom.DTO.Request.CartItemRequest;
import com.app.ecom.DTO.Response.CartItemResponse;
import com.app.ecom.Model.CartItem;
import com.app.ecom.Service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest request){

        if (!cartItemService.addToCart(userId, request)){
            return ResponseEntity.badRequest().body("Product out of stock, User not found, or Product not found");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Item added to cart successfully");
    }

    // API to reduce quantity (e.g., 5 to 3)
    @PatchMapping("/reduce")
    public ResponseEntity<String> reduceQuantity(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest request) {

        // request.getProductId() aur request.getQuantity() ka use karein
        String message = cartItemService.decreaseQuantity(
                userId,
                request.getProductId(),
                request.getQuantity()
        );

        return ResponseEntity.ok(message);
    }

    // API to remove item directly (Delete button)
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeItem(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable Long productId) {

        if (cartItemService.directRemove(userId, productId)) {
            return ResponseEntity.ok("Product removed from cart successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in cart");
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(
            @RequestHeader("X-User-ID") String userId) {

        List<CartItemResponse> response = cartItemService.getCart(userId);
        return ResponseEntity.ok(response);
    }



}
