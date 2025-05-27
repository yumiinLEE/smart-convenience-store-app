package com.ssafy.smartstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.smartstore.dto.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
	
}
