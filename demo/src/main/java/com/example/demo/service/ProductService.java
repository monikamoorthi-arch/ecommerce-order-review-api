package com.example.demo.service;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.ProductResponse;
import com.example.demo.dto.ProductReviewDto;
import com.example.demo.dto.ReviewResponse;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductImage;
import com.example.demo.entity.ProductReview;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductReviewRepository;
import com.example.demo.spec.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductReviewRepository productReviewRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getAllProducts(int page, int size) {
        Page<Product> products = productRepository.findAll(PageRequest.of(page, size));
        List<ProductResponse> content = products.stream().map(this::toResponse).toList();
        return new PageResponse<>(content, products.getNumber(), products.getSize(),
                products.getTotalElements(), products.getTotalPages());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductResponse(Long id) {
        return toResponse(getProductById(id));
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String category, Double minPrice, Double maxPrice,
                                                String keyword, Double ratings) {
        Specification<Product> spec = ProductSpecification.hasCategory(category)
                .and(ProductSpecification.priceBetween(minPrice, maxPrice))
                .and(ProductSpecification.hasNameOrDescriptionLike(keyword))
                .and(ProductSpecification.ratingGreaterThan(ratings));
        return productRepository.findAll(spec).stream().map(this::toResponse).toList();
    }

    @Transactional
    public void addReview(ProductReviewDto reviewDto) {
        Order order = orderRepository.findByOrderNo(reviewDto.getOrderNo())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + reviewDto.getOrderNo()));

        Product product = productRepository.findById(reviewDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + reviewDto.getProductId()));

        boolean productInOrder = order.getOrderItems().stream()
                .anyMatch(item -> item.getProduct().getId().equals(product.getId()));
        if (!productInOrder) {
            throw new IllegalStateException("This product is not part of the order");
        }

        if (productReviewRepository.existsByOrderIdAndProductId(order.getId(), product.getId())) {
            throw new IllegalStateException("This product has already been reviewed for this order");
        }

        ProductReview review = new ProductReview();
        review.setComment(reviewDto.getComment());
        review.setRating(reviewDto.getRating());
        review.setProduct(product);
        review.setOrder(order);
        productReviewRepository.save(review);

        int count = product.getNoOfReviews() + 1;
        double avg = (product.getRatings() * product.getNoOfReviews() + reviewDto.getRating()) / count;
        product.setNoOfReviews(count);
        product.setRatings(Math.round(avg * 10.0) / 10.0);
    }

    // Converts a Product entity into a safe response object (no loops, no lazy-loading problems)
    private ProductResponse toResponse(Product p) {
        List<String> images = p.getImages() == null ? List.of()
                : p.getImages().stream().map(ProductImage::getUrl).toList();
        List<ReviewResponse> reviews = p.getReviews() == null ? List.of()
                : p.getReviews().stream()
                .map(r -> new ReviewResponse(r.getId(), r.getRating(), r.getComment()))
                .toList();
        return new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getDescription(),
                p.getCategory(), p.getRatings(), p.getSeller(), p.getStock(),
                p.getNoOfReviews(), images, reviews);
    }
}





















//package com.example.demo.service;
//
//import com.example.demo.dto.ProductDto;
//import com.example.demo.dto.ProductImageDto;
//import com.example.demo.dto.ProductReviewDto;
//import com.example.demo.entity.Product;
//import com.example.demo.entity.ProductReview;
//import com.example.demo.repository.ProductRepository;
//import com.example.demo.repository.ProductReviewRepository;
//import com.example.demo.spec.ProductSpecification;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//
//import org.springframework.data.domain.Pageable;
//
//import javax.swing.text.html.HTMLDocument;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//public class ProductService {
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private ProductReviewRepository productReviewRepository;
//
//    public Map<String, Object> getAllProducts(int page, int size){
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Product> products = productRepository.findAll(pageable);
//        List<ProductDto> productDtos = products.stream().map(this::convertToDto).collect(Collectors.toList());
//        Map <String, Object> response = new HashMap();
//        response.put("products", productDtos);
//        response.put("totalproducts", products.getTotalElements());
//        return response;
//    }
//    public ProductDto convertToDto(Product product){
//          ProductDto dto = new ProductDto();
//          dto.setId(product.getId());
//          dto.setName(product.getName());
//          dto.setPrice(product.getPrice());
//          dto.setDescription(product.getDescription());
//          dto.setRatings(product.getRatings());
//          dto.setCategory(product.getCategory());
//          dto.setSeller(product.getSeller());
//          dto.setStock(product.getStock());
////          dto.setNumOfReviews(product.getNumOfReviews());
//
//          List<ProductReviewDto> reviewDtos = product.getReviews().stream().map(review -> {
//              ProductReviewDto reviewDto = new ProductReviewDto();
//              reviewDto.setProductId(review.getId());
//              reviewDto.setComment(review.getComment());
//              reviewDto.setRating(review.getRating());
//              return reviewDto;
//          }).collect(Collectors.toList());
//          dto.setReviews(reviewDtos);
//
////        List<ProductImageDto> imageDtos = product.getImages().stream().map(image -> {
////            ProductImageDto imageDto = new ProductImageDto(image.getPublicId());
////            return imageDto;
////        }).collect(Collectors.toList());
////        dto.setImages(imageDtos);
//          return dto;
//    }
//
//    public Product getProductById(Long id){
//        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("product not found" + id));
//    }
//
//    public List<Product> searchProducts(String category, Double minPrice, Double maxPrice, String keyword, Double ratings) {
//        Specification<Product> spec = Specification.where(ProductSpecification.hasCategory(category))
//                .and(ProductSpecification.priceBetween(minPrice, maxPrice))
//                .and(ProductSpecification.hasNameOrDescriptionLike(keyword))
//                .and(ProductSpecification.ratingGreaterThan(ratings));
//        return productRepository.findAll(spec);
//    }
//
//    public void addReview(ProductReviewDto reviewDto) {
//        Product product = productRepository.findById(reviewDto.getProductId()). orElseThrow( () -> new RuntimeException("product not found"));
//
//        ProductReview review = new ProductReview();
//        review.setComment(reviewDto.getComment());
//        review.setRating(reviewDto.getRating());
//        review.setProduct(product);
//        productReviewRepository.save(review);
//    }
//}
