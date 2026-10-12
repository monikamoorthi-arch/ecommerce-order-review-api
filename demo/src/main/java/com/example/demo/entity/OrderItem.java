package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class OrderItem {

    @Id // primary key annotation
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer quantity;
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public OrderItem(String name, Integer quantity, String image, Double price, Product product) {
        this.name = name;
        this.quantity = quantity;
        this.image = image;
        this.price = price;
        this.product = product;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public OrderItem() {
        super();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    private Double price;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
