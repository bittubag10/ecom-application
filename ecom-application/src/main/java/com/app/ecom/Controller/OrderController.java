package com.app.ecom.Controller;

import com.app.ecom.DTO.Response.OrderResponse;
import com.app.ecom.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

//    @PostMapping
//    public ResponseEntity<OrderResponse>createOrder(
//            @RequestHeader("X-User-id")String userId){
//        OrderResponse response=orderService.createOrder(userId);
//
//        return new ResponseEntity<OrderResponse>(response, HttpStatus.CREATED);
//
//    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestHeader("X-User-id") String userId) {
        OrderResponse response = orderService.createOrder(userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/history")
    public ResponseEntity<List<OrderResponse>> getOrderHistory(
            @RequestHeader("X-User-id") String userId) {

        List<OrderResponse> history = orderService.getOrderHistory(userId);
        return ResponseEntity.ok(history);
    }
}
