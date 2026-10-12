package com.example.demo.dto;

import java.util.List;

public record OrderResponse(String orderNo, String status, Double totalItemsAmount,
                            Double taxAmount, double totalAmount, List<OrderItemResponse> items) {}