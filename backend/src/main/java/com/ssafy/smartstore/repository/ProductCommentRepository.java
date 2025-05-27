package com.ssafy.smartstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.smartstore.dto.ProductComment;

import java.util.List;

public interface ProductCommentRepository extends JpaRepository<ProductComment, Integer> {
    List<ProductComment> findByProductId(int productId);
    List<ProductComment> findByUserId(String userId);
}