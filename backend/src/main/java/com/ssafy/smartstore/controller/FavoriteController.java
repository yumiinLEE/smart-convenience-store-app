package com.ssafy.smartstore.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.smartstore.dto.FavoriteRequest;
import com.ssafy.smartstore.service.FavoriteService;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

	
	private static final Logger log = LoggerFactory.getLogger(FavoriteController.class);
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> addFavorite(@RequestBody FavoriteRequest request) {
    	log.debug("찜 추가");
        favoriteService.addFavorite(request.getUserId(), request.getProductId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@RequestBody FavoriteRequest request) {
    	log.debug("찜 삭제");
        favoriteService.removeFavorite(request.getUserId(), request.getProductId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Integer>> getFavorites(@PathVariable("userId") String userId) {
    	log.debug("찜 목록");
        List<Integer> favorites = favoriteService.getFavorites(userId);
        return ResponseEntity.ok(favorites);
    }
}
