package com.ssafy.smartstore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ssafy.smartstore.dto.FavoriteProduct;
import com.ssafy.smartstore.repository.FavoriteProductRepository;

@Service
public class FavoriteService {

    private final FavoriteProductRepository favoriteRepo;

    public FavoriteService(FavoriteProductRepository favoriteRepo) {
        this.favoriteRepo = favoriteRepo;
    }

    public void addFavorite(String userId, int productId) {
        if (!favoriteRepo.findByUserIdAndProductId(userId, productId).isPresent()) {
            FavoriteProduct favorite = new FavoriteProduct();
            favorite.setUserId(userId);
            favorite.setProductId(productId);
            favoriteRepo.save(favorite);
        }
    }

    public void removeFavorite(String userId, int productId) {
        favoriteRepo.findByUserIdAndProductId(userId, productId)
                .ifPresent(favoriteRepo::delete);
    }

    public List<Integer> getFavorites(String userId) {
        return favoriteRepo.findByUserId(userId)
                .stream()
                .map(FavoriteProduct::getProductId)
                .collect(Collectors.toList());
    }
}
