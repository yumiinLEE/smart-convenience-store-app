package com.ssafy.smartstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.smartstore.dto.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
