package com.example.demo.dto;

public class OrderCreated {
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    private String orderNo;

    public OrderCreated(String orderNo) {
        super();
        this.orderNo = orderNo;
    }
}
