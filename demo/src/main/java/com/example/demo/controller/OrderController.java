package com.example.demo.controller;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.OrderCreated;
import com.example.demo.dto.OrderResponse;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderCreated> createOrder(@Valid @RequestBody CreateOrderRequest orderRequest) {
        OrderCreated order = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{orderNo}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderNo) {
        return ResponseEntity.ok(orderService.getOrder(orderNo));
    }
}



















//package com.example.demo.controller;
//
//import com.example.demo.dto.CreateOrderRequest;
//import com.example.demo.dto.OrderCreated;
//import com.example.demo.dto.OrderItemDto;
//import com.example.demo.entity.Order;
//import com.example.demo.service.OrderService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/orders")
//public class OrderController {
//
//    @Autowired
//    private OrderService orderService;
//    @PostMapping
//    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest orderRequest){
//        OrderCreated order = orderService.createOrder(orderRequest);
//        return ResponseEntity.ok().body(order);
//    }
//
//    @GetMapping("/{orderNo}")
//    public ResponseEntity<?> getOrder(@PathVariable String orderNo){
//        Order order = orderService.getOrder(orderNo);
//        return ResponseEntity.ok().body(order);
//    }
//}
