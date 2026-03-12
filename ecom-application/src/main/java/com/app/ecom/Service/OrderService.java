package com.app.ecom.Service;

import com.app.ecom.DTO.Response.CartItemResponse;
import com.app.ecom.DTO.Response.OrderItemDTO;
import com.app.ecom.DTO.Response.OrderResponse;
import com.app.ecom.Enum.OrderStatus;
import com.app.ecom.Model.*;
import com.app.ecom.Model.Order;
import com.app.ecom.Model.OrderItem;
import com.app.ecom.Model.User;
import com.app.ecom.Repository.OrderRepository;
import com.app.ecom.Repository.ProductRepository;
import com.app.ecom.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

//    public Optional<OrderResponse> createOrder(String userId) {
//
//        //validate for cart item
//        List<CartItemResponse> cartItems=cartItemService.getCart(userId);
//        if (cartItems.isEmpty()){
//            return Optional.empty();
//
//        }
//
//        //validate for user
//        Optional<User>userOptional=userRepository.findById(Long.valueOf(userId));
//        if (userOptional.isEmpty()){
//            return Optional.empty();
//        }
//        User user=userOptional.get();
//
//        //calculate total price
//        BigDecimal totalPrice=cartItems.stream()
//                .map(CartItem::getPrice)
//                .reduce(BigDecimal.ZERO,BigDecimal::add);
//
//        //create order
//
//        Order order=new Order();
//        order.setUser(user);
//        order.setOrderStatus(OrderStatus.CONFIRMED);
//        order.setTotalAmount(totalPrice);
//        List<OrderItem>orderItems=cartItems.stream()
//                .map(item->new OrderItem(
//                        null,item.getProductName(),
//                        item.getQuantity(),
//                        item.getUnitPrice(),
//                        order
//                ))
//                .toList();
//        order.setItems(orderItems);
//        Order savedOrder=orderRepository.save(order);
//
//        //clear the cart
//
//        CartItemService.cleareCart(userId);
//
//        return Optional.of(mapToOrderResponse(savedOrder));
//    }
//
//    private OrderResponse mapToOrderResponse(Order order) {
//        return new OrderResponse(
//                order.getId(),
//                order.getTotalAmount(),
//                order.getOrderStatus(),
//                order.getItems().stream()
//
//        );
//    }


    @Transactional
    public OrderResponse createOrder(String userId) {
        // 1. Validate User
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Get Cart Items
        List<CartItemResponse> cartItems = cartItemService.getCart(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 3. Create Order Object
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.CONFIRMED);

        // 4. Transform Cart Items to Order Items & Update Stock
        List<OrderItem> orderItems = cartItems.stream().map(item -> {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Real-time Stock Check
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Out of stock: " + product.getName());
            }

            // Stock kam karo
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);

            return OrderItem.builder()
                    .product(product)
                    .quantity(item.getQuantity())
                    .price(item.getUnitPrice())
                    .order(order)
                    .build();
        }).collect(Collectors.toList());

        // 5. Calculate Total
        BigDecimal total = cartItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);
        order.setItems(orderItems);

        // 6. Save Order & Clear Cart
        Order savedOrder = orderRepository.save(order);
        cartItemService.clearCart(userId);

        return mapToOrderResponse(savedOrder);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> OrderItemDTO.builder()
                        .id(item.getId())
                        .productid(item.getProduct().getId())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getOrderStatus())
                .items(itemDTOs)
                .createdAt(order.getCreatedAt())
                .build();
    }


    public List<OrderResponse> getOrderHistory(String userId) {
        // 1. Check if user exists
        userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Fetch orders from DB
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(Long.valueOf(userId));

        // 3. Convert Entity to OrderResponse (DTO)
        return orders.stream()
                .map(this::mapToOrderResponse)
                .toList();
    }
}
