package com.app.ecom.Transformer;

import com.app.ecom.DTO.Response.CartItemResponse;
import com.app.ecom.Model.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartItemTransformer {

    public static CartItemResponse toResponse(CartItem item) {
        if (item == null) return null;

        return CartItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .imageUrl(item.getProduct().getImageUrl())
                .quantity(item.getQuantity())
                .unitPrice(item.getProduct().getPrice())
                // Calculation: Unit Price * Quantity
                .subTotal(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .build();
    }
}