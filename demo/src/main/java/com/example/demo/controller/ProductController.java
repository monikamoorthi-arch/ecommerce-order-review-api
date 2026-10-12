package com.example.demo.controller;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.ProductResponse;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public PageResponse<ProductResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return productService.getAllProducts(page, size);
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductResponse(id);
    }

    @GetMapping("/search")
    public List<ProductResponse> searchProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double ratings) {
        return productService.searchProducts(category, minPrice, maxPrice, keyword, ratings);
    }
}


//package com.example.demo.controller;
//
//import com.example.demo.entity.Product;
//import com.example.demo.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.awt.print.Pageable;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/products")
//public class ProductController {
//    @Autowired
//    private ProductService productService;
//
//    @GetMapping
//    public Map<String,Object> getAllProducts(@RequestParam(defaultValue = "0") int page,  @RequestParam(defaultValue = "5") int size){
//        return productService.getAllProducts(page, size);
//    }
//
//    @GetMapping("/{id}")
//    public Product getProductById(@PathVariable Long id){
//        return productService.getProductById(id);
//    }
//
//    @GetMapping("/search")
//    public List<Product> searchProducts(@RequestParam(required = false) String category, @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice, @RequestParam(required = false) String keyword, @RequestParam(required = false) Double ratings  ){
//        return productService.searchProducts(category, minPrice,maxPrice,keyword, ratings);
//    }
//
//    @GetMapping
//    public PageResponse<ProductResponse> getAllProducts(@RequestParam(defaultValue = "0") int page,
//                                                        @RequestParam(defaultValue = "5") int size) {
//        return productService.getAllProducts(page, size);
//    }
//
//    @GetMapping("/{id}")
//    public ProductResponse getProductById(@PathVariable Long id) {
//        return productService.getProductResponse(id);
//    }
//
//    @GetMapping("/search")
//    public List<ProductResponse> searchProducts(@RequestParam(required = false) String category,
//                                                @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice,
//                                                @RequestParam(required = false) String keyword, @RequestParam(required = false) Double ratings) {
//        return productService.searchProducts(category, minPrice, maxPrice, keyword, ratings);
//    }
//
//}
