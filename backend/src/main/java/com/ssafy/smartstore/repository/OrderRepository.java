package com.ssafy.smartstore.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.smartstore.dto.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	List<Order> findByUserId(String userId);
	List<Order> findByUserIdAndOrderTimeAfter(String userId, LocalDateTime fromDate);

}
