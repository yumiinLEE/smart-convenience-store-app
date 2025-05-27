package com.ssafy.smartstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.smartstore.dto.Store;
import com.ssafy.smartstore.repository.StoreRepository;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

	@Autowired
    private StoreRepository storeRepository;

    @GetMapping
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }
}
