package com.example.demo.repository;

import com.example.demo.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long>, JpaSpecificationExecutor<ProductReview> {
    boolean existsByOrderIdAndProductId(Long orderId, Long productId);
}
