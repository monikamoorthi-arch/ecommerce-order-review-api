package com.example.demo.dto;

public record OrderItemResponse(Long productId, String name, Integer quantity,
                                Double price, String image) {}