package com.example.demo.spec;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.domain.Specification;

// specification is interface not a class
public class ProductSpecification {
    public static Specification<Product> hasCategory(String category){
        return ((root, query, cb) -> category == null ? null : cb.equal(root.get("category"), category));
    }

    public static Specification<Product> priceBetween(Double min, Double max){
        return (root, query, cb)->{
            if(min == null && max == null) return null;
            if(min == null)  return cb.lessThanOrEqualTo(root.get("price"), max);
            if(max == null)  return cb.greaterThanOrEqualTo(root.get("price"), min);
            return cb.between(root.get("price"), min, max);
        };
    }

    public static Specification<Product> ratingGreaterThan(Double ratings) {
        return (root, query, cb) -> ratings == null ? null
                : cb.greaterThanOrEqualTo(root.get("ratings"), ratings);
    }

    public static Specification<Product> hasNameOrDescriptionLike(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern));
        };
    }

//    public static Specification<Product> hasNameOrDescriptionLike(String keyword){
//        return (root, query,cb) -> {
//            if(keyword == null || keyword.isEmpty()) return null;
//            return cb.or(cb.like(root.get("name"), "%" + keyword.toLowerCase() + "%"),
//                    cb.like(root.get("description"), "%" + keyword.toLowerCase() + "%"));
//        };
//    }
//
//    public static Specification<Product> ratingGreaterThan(Double ratings){
//        return (root, query, cb) -> {
//            return cb.greaterThanOrEqualTo(root.get("ratings"), ratings);
//        };
//    }
}
