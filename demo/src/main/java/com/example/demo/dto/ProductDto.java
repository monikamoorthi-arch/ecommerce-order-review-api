package com.example.demo.dto;

import com.example.demo.entity.ProductImage;
import com.example.demo.entity.ProductReview;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDto {
    private Long id;
    private String name;
    private Double price;
    private String description;
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    private String category;
    private Double ratings = 0.0;
    private String seller;
    private Integer stock = 0;
    private Integer noOfReviews = 0;
    public List<ProductImageDto> getImages() {
        return images;
    }

    public void setImages(List<ProductImageDto> images) {
        this.images = images;
    }


    private List<ProductImageDto> images;

    public List<ProductReviewDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ProductReviewDto> reviews) {
        this.reviews = reviews;
    }

    private List<ProductReviewDto> reviews;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRatings() {
        return ratings;
    }

    public void setRatings(Double ratings) {
        this.ratings = ratings;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getNoOfReviews() {
        return noOfReviews;
    }

    public void setNoOfReviews(Integer noOfReviews) {
        this.noOfReviews = noOfReviews;
    }

    public ProductDto(Long id, String name, Double price, String description, String category, Double ratings, String seller, Integer stock, List<ProductImageDto> images) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.ratings = ratings;
        this.seller = seller;
        this.stock = stock;
        this.images = images;
    }
    public ProductDto() {
        super();
    }

    // Getters and Setters
}
