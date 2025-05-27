package com.ssafy.smartstore.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.smartstore.dto.Order;
import com.ssafy.smartstore.dto.OrderRequest;
import com.ssafy.smartstore.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private OrderService orderService;

    // 주문 정보 저장
    @PostMapping
    public ResponseEntity<Order> saveOrder(@RequestBody OrderRequest request) {
        log.debug("주문 정보 저장");
        Order savedOrder = orderService.saveOrder(request);
        return ResponseEntity.ok(savedOrder);
    }

    // user의 주문 정보
    @GetMapping("/{userId}")
    public List<Order> getOrdersByUser(@PathVariable("userId") String userId) {
        log.debug("user의 주문 정보");

    	return orderService.getOrdersByUser(userId);
    }
    
    // 유저의 상품주문 내역 기간 필터링
    @GetMapping("/user/{userId}/filter")
    public ResponseEntity<List<Order>> getUserOrdersByPeriod(
    	    @PathVariable("userId") String userId,
    	    @RequestParam("months") int months) {

        List<Order> result = orderService.getUserOrdersWithinMonths(userId, months);
        return ResponseEntity.ok(result);
    }
}