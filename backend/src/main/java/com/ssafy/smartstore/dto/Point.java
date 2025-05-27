package com.ssafy.smartstore.dto;

import jakarta.persistence.*;

@Entity
@Table(name = "point")
public class Point {

    @Id
    private String id;  // UUID 등으로 사용하기 위해 String

    private String userId;

    private String orderId; // 출석 관련이므로 null 허용

    private int point;

    public Point() {}

    public Point(String id, String userId, String orderId, int point) {
        this.id = id;
        this.userId = userId;
        this.orderId = orderId;
        this.point = point;
    }

    
    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getOrderId() { return orderId; }

    public void setOrderId(String orderId) { this.orderId = orderId; }

    public int getPoint() { return point; }

    public void setPoint(int point) { this.point = point; }
}