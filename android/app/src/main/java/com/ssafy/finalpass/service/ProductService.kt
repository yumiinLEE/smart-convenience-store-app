package com.ssafy.finalpass.service

import com.ssafy.finalpass.dto.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {

    // 전체 상품 조회
    @GET("api/products")
    suspend fun getAllProducts(): Response<List<Product>>

    // 상품 이름이나 카테고리로 검색 (검색창에 직접 입력)
    @GET("api/products/search")
    suspend fun searchProducts(@Query("keyword") keyword: String): Response<List<Product>>

    // 상품 id로 상품 검색
    @GET("api/products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Product

    // home에서 category를 클릭해서 검색
    @GET("api/products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): Response<List<Product>>

}