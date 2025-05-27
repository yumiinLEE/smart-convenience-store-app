package com.ssafy.smartstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.smartstore.dto.FavoriteProduct;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Integer> {
    List<FavoriteProduct> findByUserId(String userId);
    Optional<FavoriteProduct> findByUserIdAndProductId(String userId, int productId);
}
