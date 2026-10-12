package com.example.demo.entity;

import com.example.demo.dto.ProductReviewDto;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "products")
public class Product {
    @Id // primary key annotation
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name field is required")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "price field is required")
    @PositiveOrZero(message = "value must be zero or greater than zero")
    private Double price;

    @NotBlank(message = "Description field is required")
    private String description;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;
    private Double ratings = 0.0;

    @NotBlank(message = "Seller field is required")
    private String seller;

    @NotNull(message = "Stock field is required")
    private Integer stock = 0;
    private Integer noOfReviews = 0;

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }


    //cascade means whatever happen(update) to product(parent table) it affect child(productimage) also
    //orphanremoval = true means if in product table delete any record, it also delete related data frp, prodictimage also
    //foreign key name
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ProductImage> images;

    public List<ProductReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<ProductReview> reviews) {
        this.reviews = reviews;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ProductReview> reviews;

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

    public Product() {
        super();
    }

    public Product(Long id, String name, Double price, String description, String category, Double ratings, String seller, Integer stock, List<String> images) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.ratings = ratings;
        this.seller = seller;
        this.stock = stock;
        this.images = images.stream().map(url -> new ProductImage(url, this)).collect(Collectors.toList());
    }

}
// herbernet JPA causes create columns in db whatever we create in entity file