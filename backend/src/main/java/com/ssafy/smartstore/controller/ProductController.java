package com.ssafy.smartstore.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.smartstore.dto.Product;
import com.ssafy.smartstore.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductRepository productRepository;
    
    // 전체 상품 조회
    @GetMapping
    public List<Product> getAllProducts() {
    	log.debug("전체 상품 조회");
    	return productRepository.findAll();
    }
    
    // 상품 이름이나 카테고리로 검색
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam("keyword") String keyword) {
    	log.debug("상품 검색");
        return productRepository.searchByKeyword(keyword);
    }
    
    // 상품id로 상품정보조회
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") int id) {
        return productRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    // 카테고리 기반 검색
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable("category") String category) {
    	log.debug("카테고리별 상품 조회");

    	return ResponseEntity.ok(productRepository.findByCategoryIgnoreCase(category));
    }
   
}
