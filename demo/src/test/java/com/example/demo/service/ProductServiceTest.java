package com.example.demo.service;

import com.example.demo.dto.ProductReviewDto;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductReview;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductReviewRepository productReviewRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private ProductService productService;

    private Product product(Long id, double ratings, int noOfReviews) {
        Product p = new Product();
        p.setId(id);
        p.setName("Test Phone");
        p.setPrice(100.0);
        p.setRatings(ratings);
        p.setNoOfReviews(noOfReviews);
        return p;
    }

    private Order orderContaining(Product p) {
        Order order = new Order();
        order.setOrderNo("ORD-1");
        order.getOrderItems().add(new OrderItem(p.getName(), 1, null, p.getPrice(), p));
        return order;
    }

    private ProductReviewDto reviewFor(Long productId, double rating) {
        ProductReviewDto dto = new ProductReviewDto();
        dto.setOrderNo("ORD-1");
        dto.setProductId(productId);
        dto.setComment("Great");
        dto.setRating(rating);
        return dto;
    }

    @Test
    void addReview_savesReview_andUpdatesAverageRating() {
        Product p = product(1L, 4.0, 1);               // one existing review of 4.0
        Order order = orderContaining(p);
        when(orderRepository.findByOrderNo("ORD-1")).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));
        when(productReviewRepository.existsByOrderIdAndProductId(any(), any())).thenReturn(false);

        productService.addReview(reviewFor(1L, 5.0));

        ArgumentCaptor<ProductReview> captor = ArgumentCaptor.forClass(ProductReview.class);
        verify(productReviewRepository).save(captor.capture());
        assertSame(order, captor.getValue().getOrder());
        assertSame(p, captor.getValue().getProduct());
        assertEquals(2, p.getNoOfReviews());            // 1 -> 2
        assertEquals(4.5, p.getRatings());              // (4.0 + 5.0) / 2
    }

    @Test
    void addReview_rejectsProductThatIsNotInTheOrder() {
        Product inOrder = product(1L, 0.0, 0);
        Product other = product(2L, 0.0, 0);
        when(orderRepository.findByOrderNo("ORD-1")).thenReturn(Optional.of(orderContaining(inOrder)));
        when(productRepository.findById(2L)).thenReturn(Optional.of(other));

        assertThrows(IllegalStateException.class,
                () -> productService.addReview(reviewFor(2L, 4.0)));

        verify(productReviewRepository, never()).save(any());
        assertEquals(0, other.getNoOfReviews());
    }

    @Test
    void addReview_rejectsDuplicateReviewForSameOrderAndProduct() {
        Product p = product(1L, 4.0, 1);
        when(orderRepository.findByOrderNo("ORD-1")).thenReturn(Optional.of(orderContaining(p)));
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));
        when(productReviewRepository.existsByOrderIdAndProductId(any(), any())).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> productService.addReview(reviewFor(1L, 5.0)));

        verify(productReviewRepository, never()).save(any());
        assertEquals(1, p.getNoOfReviews());            // unchanged
        assertEquals(4.0, p.getRatings());
    }

    @Test
    void addReview_throwsWhenOrderNotFound() {
        when(orderRepository.findByOrderNo("ORD-1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.addReview(reviewFor(1L, 5.0)));

        verify(productReviewRepository, never()).save(any());
    }
}