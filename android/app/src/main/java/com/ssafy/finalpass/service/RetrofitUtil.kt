package com.ssafy.finalpass.service

import com.ssafy.finalpass.base.ApplicationClass.Companion.retrofit

class RetrofitUtil {
    companion object {
        val attendanceService = retrofit.create(AttendanceService::class.java)
        val commentService = retrofit.create(CommentService::class.java)
        val orderService = retrofit.create(OrderService::class.java)
        val productService = retrofit.create(ProductService::class.java)
        val userService = retrofit.create(UserService::class.java)
        val storeService = retrofit.create(StoreService::class.java)
        val favoriteService = retrofit.create(FavoriteService::class.java)
    }

}