package com.ssafy.smartstore.controller;

import com.ssafy.smartstore.dto.ProductComment;
import com.ssafy.smartstore.service.ProductCommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class ProductCommentController {

    @Autowired
    private ProductCommentService commentService;

    // 리뷰 등록
    @PostMapping
    public ResponseEntity<Void> postComment(@RequestBody ProductComment comment) {
        commentService.save(comment);
        return ResponseEntity.ok().build();
    }

    // 상품 ID로 댓글 조회
    @GetMapping("/{productId}")
    public ResponseEntity<List<ProductComment>> getComments(@PathVariable("productId") int productId) {
        return ResponseEntity.ok(commentService.getCommentsByProductId(productId));
    }
    
    // 사용자 ID로 댓글 조회
    @GetMapping("/user/{userId}")
    public List<ProductComment> getCommentsByUser(@PathVariable("userId") String userId) {
        return commentService.getCommentsByUserId(userId);
    }
    
    // 리뷰 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") int commentId) {
    	commentService.deleteById(commentId);
        return ResponseEntity.ok().build();
    }

    // 리뷰 수정
    @PutMapping
    public ResponseEntity<Void> updateComment(@RequestBody ProductComment updatedComment) {
        Optional<ProductComment> commentOpt = commentService.findById(updatedComment.getId());
        if (commentOpt.isPresent()) {
            ProductComment existing = commentOpt.get();
            existing.setRating(updatedComment.getRating());
            existing.setComment(updatedComment.getComment());
            commentService.save(existing);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    // 상품 리뷰 불러오기
    @GetMapping
    public ResponseEntity<List<ProductComment>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }
}
