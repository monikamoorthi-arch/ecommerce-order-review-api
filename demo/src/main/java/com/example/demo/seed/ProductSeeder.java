package com.example.demo.seed;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.SQLOutput;
import java.util.List;

@Component
public class ProductSeeder implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if(productRepository.count() == 0){
            List<Product> demoProducts = List.of(
                    new Product(null, "Apple iPhone 15", 799.0, "Smartphone with A16 chip","Phones",  4.8, "Amazon", 5, List.of("/products/iphone15.jpeg")),
                    new Product(null, "Samsung Galaxy S24", 749.0, "Flagship Android smartphone", "Laptops", 4.7, "Amazon", 8, List.of("/products/galaxys24.jpeg")),
                    new Product(null, "Google Pixel 9", 699.0, "Google AI powered smartphone","Audio", 4.6, "Flipkart", 6, List.of("/products/googlepixel9.jpeg")),
                    new Product(null, "OnePlus 13", 649.0, "Fast performance flagship phone","Laptops", 4.5, "Amazon", 10, List.of("/products/1plus13.jpeg")),
                    new Product(null, "Xiaomi 15 Pro", 599.0, "Premium smartphone with Leica camera","Camera", 4.4, "Flipkart", 7, List.of("/products/xiaomi15pro.jpeg")),
                    new Product(null, "Sony WH-1000XM5", 349.0, "Noise cancelling headphones","Phones", 4.9, "Amazon", 12, List.of("/products/sonyWH.jpeg")),
                    new Product(null, "Apple Watch Series 10", 399.0, "Advanced smartwatch","Audio",  4.8, "Amazon", 4, List.of("/products/applewatch10.jpeg")),
                    new Product(null, "Samsung Galaxy Watch 7", 299.0, "Fitness and health smartwatch","Phones", 4.6, "Flipkart", 9, List.of("/products/galaxywatch7.jpg")),
                    new Product(null, "Dell XPS 15", 1499.0, "High-performance laptop","Laptops",  4.7, "Amazon", 3, List.of("/products/delxps15.jpeg")),
                    new Product(null, "MacBook Air M4", 1299.0, "Lightweight Apple laptop","Laptops",  4.9, "Amazon", 2, List.of("/products/macbookM4.jpeg")),
                    new Product(null, "iPad Air M3", 699.0, "Powerful tablet for productivity","Phones",  4.8, "Amazon", 5, List.of())
            );
            productRepository.saveAll(demoProducts);
            System.out.println("seeded demo products");
        }else{
            System.out.println("products already exist");
        }
    }
}
