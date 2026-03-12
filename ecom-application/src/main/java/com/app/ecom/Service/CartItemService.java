package com.app.ecom.Service;

import com.app.ecom.DTO.Request.CartItemRequest;
import com.app.ecom.DTO.Response.CartItemResponse;
import com.app.ecom.Model.CartItem;
import com.app.ecom.Model.Product;
import com.app.ecom.Model.User;
import com.app.ecom.Repository.CartItemRepository;
import com.app.ecom.Repository.ProductRepository;
import com.app.ecom.Repository.UserRepository;
import com.app.ecom.Transformer.CartItemTransformer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Transactional
    public void clearCart(String userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        cartItemRepository.deleteByUser(user);
    }

//    public Boolean addToCart(String userId, CartItemRequest request) {
//        Optional<Product>productOpt=productRepository.findById(request.getProductId());
//        if (productOpt.isEmpty()){
//            return false;
//        }
//        Product product=productOpt.get();
//
//        if (product.getStockQuantity()< request.getQuantity()){
//            return false;
//        }
//        Optional<User> userOpt=userRepository.findById(Long.valueOf(userId));
//        if (userOpt.isEmpty()){
//            return false;
//        }
//        User user=userOpt.get();
//
//        CartItem  existingCartItem=cartItemRepository.findByUserAndProduct(user,product);
//
//        if (existingCartItem !=null){
//            existingCartItem.setQuantity(existingCartItem.getQuantity()+request.getQuantity());
//            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
//            cartItemRepository.save(existingCartItem);
//        }else {
//            CartItem cartItem=new CartItem();
//            cartItem.setUser(user);
//            cartItem.setProduct(product);
//            cartItem.setQuantity(request.getQuantity());
//            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
//            cartItemRepository.save(cartItem);
//        }
//        return true;
//    }

    @Transactional
    public Boolean addToCart(String userId, CartItemRequest request) {
        // 1. Product find
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if (productOpt.isEmpty()) return false;
        Product product = productOpt.get();

        // 2. User search
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if (userOpt.isEmpty()) return false;
        User user = userOpt.get();

        // 3.  product already exist in cart?
        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingCartItem != null) {
            // --- FIX START ---
            // Nayi total quantity calculate karo (Cart mein pehle se jo hai + Request wali)
            int totalQuantityInCart = existingCartItem.getQuantity() + request.getQuantity();

            // Check karo ki total quantity stock se zyada toh nahi ho rahi
            if (totalQuantityInCart > product.getStockQuantity()) {
                System.out.println("Error: Total quantity in cart cannot exceed stock!");
                return false; // Stop! Stock se zyada add nahi karne denge
            }
            // --- FIX END ---

            existingCartItem.setQuantity(totalQuantityInCart);
            // Hamesha base price (product.getPrice()) se multiply karein
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(totalQuantityInCart)));
            cartItemRepository.save(existingCartItem);
        } else {
            // Naya item add karte waqt bhi check karo
            if (request.getQuantity() > product.getStockQuantity()) {
                return false;
            }

            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);
        }
        return true;
    }


    // 1. Decrease Quantity Logic
    @Transactional
    public String decreaseQuantity(String userId, Long productId, Integer quantityToReduce) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);

        if (cartItem == null) {
            return "Item not found in your cart";
        }

        int updatedQuantity = cartItem.getQuantity() - quantityToReduce;

        if (updatedQuantity <= 0) {
            // Agar 0 ya minus mein jaye toh item delete kar do
            cartItemRepository.delete(cartItem);
            return "Item removed from cart completely";
        } else {
            // Quantity update karo aur price refresh karo
            cartItem.setQuantity(updatedQuantity);
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(updatedQuantity)));
            cartItemRepository.save(cartItem);
            return "Quantity decreased. Current quantity: " + updatedQuantity;
        }
    }

    // 2. Direct Remove Logic
    @Transactional
    public Boolean directRemove(String userId, Long productId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);
        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }


    public List<CartItemResponse> getCart(String userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        return cartItems.stream()
                .map(CartItemTransformer::toResponse) // Sahi call: transformer instance se
                .collect(Collectors.toList());
    }
}
