package com.example.demo.service;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.OrderCreated;
import com.example.demo.dto.OrderItemDto;
import com.example.demo.dto.OrderItemResponse;
import com.example.demo.dto.OrderResponse;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Product;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private static final double TAX = 10;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderCreated createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setStatus("PENDING");
        double itemsTotal = 0;

        for (OrderItemDto item : request.getOrderitems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + item.getProductId()));

            if (product.getStock() < item.getQuantity()) {
                throw new IllegalStateException("Not enough stock for " + product.getName());
            }
            product.setStock(product.getStock() - item.getQuantity());

//            String image = product.getImages().isEmpty() ? null : product.getImages().get(0).getUrl();
            String image = (product.getImages() == null || product.getImages().isEmpty())
                    ? null : product.getImages().get(0).getUrl();
            order.getOrderItems().add(new OrderItem(product.getName(), item.getQuantity(),
                    image, product.getPrice(), product));
            itemsTotal += product.getPrice() * item.getQuantity();
        }

        order.setTotalItemsAmount(itemsTotal);
        order.setTaxAmount(TAX);
        order.setTotalAmount(itemsTotal + TAX);
        order.setOrderNo(UUID.randomUUID().toString());
        orderRepository.save(order);
        return new OrderCreated(order.getOrderNo());
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderNo));

        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(i -> new OrderItemResponse(i.getProduct().getId(), i.getName(),
                        i.getQuantity(), i.getPrice(), i.getImage()))
                .toList();

        return new OrderResponse(order.getOrderNo(), order.getStatus(), order.getTotalItemsAmount(),
                order.getTaxAmount(), order.getTotalAmount(), items);
    }
}






















//package com.example.demo.service;
//
//import com.example.demo.dto.CreateOrderRequest;
//import com.example.demo.dto.OrderCreated;
//import com.example.demo.dto.OrderItemDto;
//import com.example.demo.entity.Order;
//import com.example.demo.entity.OrderItem;
//import com.example.demo.entity.Product;
//import com.example.demo.repository.OrderRepository;
//import com.example.demo.repository.ProductRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//@Service
//public class OrderService {
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private OrderRepository orderRepository;
//
//    public OrderCreated createOrder(CreateOrderRequest orderRequest ){
//        Order order = new Order();
//        order.setStatus("PENDING");
//        double totalItemsAmount = 0;
//
//        for(OrderItemDto item: orderRequest.getOrderitems()){
//            OrderItem orderItem = new OrderItem();
//            orderItem.setName(item.getName());
//            orderItem.setPrice(item.getPrice());
//            orderItem.setQuantity(item.getQuantity());
//            orderItem.setImage(item.getImage());
//            Product product = productRepository.findById(item.getProductId()).orElseThrow( () -> new RuntimeException("product not found"));
//            orderItem.setProduct(product);
//            totalItemsAmount += item.getPrice() * item.getQuantity();
//
//            order.getOrderItems().add(orderItem);
//        }
//        order.setTotalItemsAmount(totalItemsAmount);
//        double totalAmount = 0;
//        double taxAmount = 10;
//        totalAmount = totalItemsAmount + taxAmount;
//        order.setTotalAmount(totalAmount);
//        order.setTaxAmount(taxAmount);
//        String ordernum = UUID.randomUUID().toString();
//        order.setOrderNo(ordernum);
//        return new OrderCreated(ordernum);
//    }
//
//    public Order getOrder(String orderNo) {
//        return orderRepository.findByOrderNo(orderNo).orElseThrow( () -> new RuntimeException("Order not found"));
//    }
//}
