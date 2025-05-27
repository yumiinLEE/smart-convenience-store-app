package com.ssafy.smartstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.smartstore.dto.Product;
import com.ssafy.smartstore.repository.ProductRepository;

@Service
public class ProductService {
	
    @Autowired
    private ProductRepository productRepository;

    
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

}
