package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreateOrderRequest {
    @NotEmpty(message = "at least one order item is required")
    @Valid
    private List<OrderItemDto> orderitems;

    public List<OrderItemDto> getOrderitems() {
        return orderitems;
    }

    public void setOrderitems(List<OrderItemDto> orderitems) {
        this.orderitems = orderitems;
    }
}








//package com.example.demo.dto;
//
//import java.util.List;
//
//public class CreateOrderRequest {
//    private List<OrderItemDto> orderitems;
//
//    public List<OrderItemDto> getOrderitems() {
//        return orderitems;
//    }
//
//    public void setOrderitems(List<OrderItemDto> orderitems) {
//        this.orderitems = orderitems;
//    }
//}
