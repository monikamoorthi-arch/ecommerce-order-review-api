package com.example.demo.dto;

import java.util.List;

public record ProductResponse(Long id, String name, Double price, String description,
                              String category, Double ratings, String seller, Integer stock,
                              Integer noOfReviews, List<String> images,
                              List<ReviewResponse> reviews) {}
