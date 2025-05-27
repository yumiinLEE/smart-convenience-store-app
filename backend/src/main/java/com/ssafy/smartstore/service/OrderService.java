package com.ssafy.smartstore.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.smartstore.dto.Order;
import com.ssafy.smartstore.dto.OrderDetail;
import com.ssafy.smartstore.dto.OrderRequest;
import com.ssafy.smartstore.dto.Product;
import com.ssafy.smartstore.repository.OrderRepository;
import com.ssafy.smartstore.repository.ProductRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ProductRepository productRepository;

    // 주문과 주문상세 저장
    // OrderRequest → Order 변환 + 상품 정보 추가 + 저장
    public Order saveOrder(OrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStoreName(request.getStoreName());
        order.setOrderTime(LocalDateTime.now());

        List<OrderDetail> detailList = new ArrayList<>();
        for (OrderRequest.OrderDetailRequest d : request.getDetails()) {
            OrderDetail detail = new OrderDetail();
            detail.setProductId(d.getProductId());
            detail.setQuantity(d.getQuantity());
            detail.setUnitPrice(d.getUnitPrice());

            // 상품 정보 조회
            Product product = productRepository.findById(d.getProductId())
                    .orElseThrow(() -> new RuntimeException("상품 정보 없음: " + d.getProductId()));

            detail.setProductName(product.getName());
            detail.setImg(product.getImg());

            detail.setOrder(order); // 양방향 연관관계 설정
            detailList.add(detail);
        }

        order.setDetails(detailList);
        return orderRepository.save(order); // cascade로 함께 저장됨
    }

    public List<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId);
    }
    
    public List<Order> getUserOrdersWithinMonths(String userId, int months) {
        LocalDateTime fromDate = LocalDateTime.now().minusMonths(months);
        return orderRepository.findByUserIdAndOrderTimeAfter(userId, fromDate);
    }
}