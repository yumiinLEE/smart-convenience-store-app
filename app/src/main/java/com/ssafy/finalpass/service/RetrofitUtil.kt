package com.ssafy.finalpass.service

import com.ssafy.finalpass.service.UserService
import com.ssafy.finalpass.base.ApplicationClass

class RetrofitUtil {
    companion object{
        val attendanceService = ApplicationClass.retrofit.create(AttendanceService::class.java)
        val commentService = ApplicationClass.retrofit.create(CommentService::class.java)
        val orderService = ApplicationClass.retrofit.create(OrderService::class.java)
        val productService = ApplicationClass.retrofit.create(ProductService::class.java)
        val userService = ApplicationClass.retrofit.create(UserService::class.java)
        val storeService: StoreService = ApplicationClass.retrofit.create(StoreService::class.java)
    }
}