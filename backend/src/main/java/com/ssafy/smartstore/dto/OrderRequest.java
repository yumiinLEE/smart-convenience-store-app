package com.ssafy.smartstore.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ssafy.smartstore.dto.Order;
import com.ssafy.smartstore.dto.OrderDetail;

public class OrderRequest {

    private String userId;
    private String storeName;
    private List<OrderDetailRequest> details;

    // Getter & Setter
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<OrderDetailRequest> getDetails() {
        return details;
    }
    public void setDetails(List<OrderDetailRequest> details) {
        this.details = details;
    }

    // DTO → Entity 변환
    public Order toEntity() {
        Order order = new Order();
        order.setUserId(this.userId);
        order.setStoreName(this.storeName);
        order.setOrderTime(LocalDateTime.now());

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetailRequest d : details) {
            OrderDetail detail = new OrderDetail();
            detail.setProductId(d.getProductId());
            detail.setQuantity(d.getQuantity());
            detail.setUnitPrice(d.getUnitPrice());

            // 양방향 관계 설정
            detail.setOrder(order);
            orderDetails.add(detail);
        }

        order.setDetails(orderDetails); // cascade로 함께 저장됨
        return order;
    }

    // 내부 클래스: 주문 상세 요청
    public static class OrderDetailRequest {
        private int productId;
        private int quantity;
        private int unitPrice;

        // Getter & Setter
        public int getProductId() {
            return productId;
        }
        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getUnitPrice() {
            return unitPrice;
        }
        public void setUnitPrice(int unitPrice) {
            this.unitPrice = unitPrice;
        }
    }
}
