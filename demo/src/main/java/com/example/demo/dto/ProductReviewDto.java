package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public class ProductReviewDto {

    @NotNull(message = "rating is required")
    @DecimalMin(value = "1.0", message = "rating must be at least 1")
    @DecimalMax(value = "5.0", message = "rating must be at most 5")
    private Double rating;
    @NotNull(message = "Product id is required")
    private Long productId;

    @NotBlank(message = "orderNo is required")
    private String orderNo;

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @NotBlank(message = "comment cannot be blank")
    private String comment;

    public ProductReviewDto(Long productId, String comment, Double rating) {
        this.productId = productId;
        this.comment = comment;
        this.rating = rating;
    }
    public ProductReviewDto(){
        super();
    }
// Getters and Setters
}
