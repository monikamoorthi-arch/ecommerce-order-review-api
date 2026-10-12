package com.example.demo.controller;

import com.example.demo.dto.ProductReviewDto;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductReview;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/products/reviews")
public class ProductReviewController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<?> addReviews(@RequestBody @Valid ProductReviewDto reviewDto ){
        productService.addReview(reviewDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("review added");
    }
}
