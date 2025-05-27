package com.ssafy.smartstore.dto;

public class FavoriteRequest {
    private String userId;
    private int productId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
    
    
}
