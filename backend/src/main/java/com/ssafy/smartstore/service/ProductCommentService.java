package com.ssafy.smartstore.service;

import com.ssafy.smartstore.dto.ProductComment;
import com.ssafy.smartstore.dto.User;
import com.ssafy.smartstore.repository.ProductCommentRepository;
import com.ssafy.smartstore.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCommentService {

    @Autowired
    private ProductCommentRepository commentRepository;
    
    @Autowired
    private UserRepository userRepository;

    // 상품 후기 저장
    public ProductComment save(ProductComment comment) {
        // 사용자 이름 조회
        String userName = userRepository.findById(comment.getUserId())
            .map(User::getName)
            .orElse("알 수 없음");

        comment.setUserName(userName);

        return commentRepository.save(comment);
    }

    // 상품id로 해당 상품의 전체 리뷰 조회
    public List<ProductComment> getCommentsByProductId(int productId) {
        return commentRepository.findByProductId(productId);
    }
    
    // 사용자 ID로 리뷰 목록 조회
    public List<ProductComment> getCommentsByUserId(String userId) {
        return commentRepository.findByUserId(userId);
    }
    
    // 리뷰 삭제
    public void deleteById(int id) {
        commentRepository.deleteById(id);
    }

    // ID로 리뷰 찾기 (수정용)
    public Optional<ProductComment> findById(int id) {
        return commentRepository.findById(id);
    }
    
    // 리뷰전체 조회
    public List<ProductComment> getAllComments() {
        return commentRepository.findAll();
    }
}
