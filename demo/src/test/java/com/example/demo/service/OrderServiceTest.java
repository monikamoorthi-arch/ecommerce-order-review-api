package com.example.demo.service;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.OrderCreated;
import com.example.demo.dto.OrderItemDto;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderService orderService;

    private Product product(Long id, double price, int stock) {
        Product p = new Product();
        p.setId(id);
        p.setName("Test Phone");
        p.setPrice(price);
        p.setStock(stock);
        p.setImages(new ArrayList<>());
        return p;
    }

    private CreateOrderRequest request(Long productId, int quantity, Double clientPrice) {
        OrderItemDto item = new OrderItemDto();
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setPrice(clientPrice);
        CreateOrderRequest request = new CreateOrderRequest();
        request.setOrderitems(List.of(item));
        return request;
    }

    @Test
    void createOrder_usesDatabasePrice_notClientPrice_andReducesStock() {
        Product p = product(1L, 100.0, 10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        // client tries to pay 1.0 instead of 100.0
        OrderCreated result = orderService.createOrder(request(1L, 2, 1.0));

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(captor.capture());
        Order saved = captor.getValue();

        assertNotNull(result.getOrderNo());
        assertEquals(200.0, saved.getTotalItemsAmount());   // 100 x 2, not 1 x 2
        assertEquals(210.0, saved.getTotalAmount());        // plus 10 tax
        assertEquals(100.0, saved.getOrderItems().get(0).getPrice());
        assertEquals(8, p.getStock());                      // 10 - 2
    }

    @Test
    void createOrder_throwsWhenStockIsInsufficient() {
        Product p = product(1L, 100.0, 1);
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        assertThrows(IllegalStateException.class,
                () -> orderService.createOrder(request(1L, 5, 100.0)));

        verify(orderRepository, never()).save(any());
        assertEquals(1, p.getStock());                      // unchanged
    }

    @Test
    void createOrder_throwsWhenProductNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.createOrder(request(99L, 1, 100.0)));

        verify(orderRepository, never()).save(any());
    }
}