package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "product_review",
        uniqueConstraints = @UniqueConstraint(columnNames = {"order_id", "product_id"}))

public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(value = 0)
    @Max(value = 5)
    private Double rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ProductReview() {
        super();
    }

    public ProductReview(Long id, Double rating, String comment) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
