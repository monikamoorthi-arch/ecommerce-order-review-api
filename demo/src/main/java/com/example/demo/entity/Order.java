package com.example.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id // primary key annotation
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();


    private Double totalItemsAmount;
    private Double taxAmount;
    private double totalAmount;
    private String status;
    private String orderNo;

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Double getTotalItemsAmount() {
        return totalItemsAmount;
    }

    public void setTotalItemsAmount(Double totalItemsAmount) {
        this.totalItemsAmount = totalItemsAmount;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getId() {
        return id;
    }

    public Order(List<OrderItem> orderItems, Double totalItemsAmount, Double taxAmount, double totalAmount, String status, String orderNo) {
        this.orderItems = orderItems;
        this.totalItemsAmount = totalItemsAmount;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderNo = orderNo;
    }

    public Order() {
        super();
    }
}
