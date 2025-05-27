package com.ssafy.finalpass

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.finalpass.dto.Order
import com.ssafy.finalpass.dto.OrderDetail
import com.ssafy.finalpass.dto.OrderRequest
import com.ssafy.finalpass.dto.Product
import com.ssafy.finalpass.dto.ProductComment
import com.ssafy.finalpass.dto.Store
import com.ssafy.finalpass.dto.User
import com.ssafy.finalpass.dto.UserRequest
import com.ssafy.finalpass.service.RetrofitUtil
import com.ssafy.finalpass.service.RetrofitUtil.Companion.attendanceService
import com.ssafy.finalpass.service.RetrofitUtil.Companion.commentService
import com.ssafy.finalpass.service.RetrofitUtil.Companion.orderService
import com.ssafy.finalpass.service.RetrofitUtil.Companion.productService
import com.ssafy.finalpass.service.RetrofitUtil.Companion.storeService
import com.ssafy.finalpass.service.RetrofitUtil.Companion.userService
import kotlinx.coroutines.launch


class MainActivityViewModel : ViewModel() {

    //////////////////////////////////// user ////////////////////////////////////
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> = _registerResult

    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> = _loginResult

    // 회원가입
    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch {
            try {
                val response = userService.registerUser(userRequest)
                if (response.isSuccessful) {
                    val message = response.body()?.string() ?: "회원가입 성공"
                    _registerResult.value = Result.success(message)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "알 수 없는 오류"
                    _registerResult.value = Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
                }
        }
    }

    // 로그인
    fun login(id: String, password: String) {
        viewModelScope.launch {
            try {
                val userRequest = UserRequest(id = id, pass = password, name = "")
                val response = userService.login(userRequest)

                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        _loginResult.value = Result.success(user)
                    } else {
                        _loginResult.value = Result.failure(Exception("로그인 실패: 사용자 정보 없음"))
                    }
                } else if (response.code() == 401) {
                    _loginResult.value = Result.failure(Exception("아이디 또는 비밀번호가 올바르지 않습니다."))
                } else {
                    _loginResult.value = Result.failure(Exception("로그인 실패: ${response.errorBody()?.string()}"))
                }

            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }

    // 유저정보 id로 조회
    fun getUser(id: String) {
        viewModelScope.launch {
            try {
                val result = userService.getUser(id)
                _user.value = result
                Log.d("ViewModel", "유저 정보 불러오기 성공: $result")

            } catch (e: Exception) {
                Log.e("UserViewModel", "사용자 정보 조회 실패: ${e.message}")
            }
        }
    }

    // 닉네임 정보 수정
    fun updateUserName(userId: String, newName: String) {
        viewModelScope.launch {
            try {
                val updatedUser = RetrofitUtil.userService.updateUserName(
                    userId,
                    mapOf("name" to newName)
                )
                _user.value = updatedUser  // LiveData 갱신 → UI 자동 반영
            } catch (e: Exception) {
                Log.e("ViewModel", "닉네임 수정 실패", e)
            }
        }
    }


    fun clearUser() {
        _user.value = null
    }


    //////////////////////////////////// product ////////////////////////////////////
    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>> = _productList

    // 모든 상품 목록 조회
    fun getAllProduct() {
        viewModelScope.launch {
            try {
                val response = productService.getAllProducts()
                if (response.isSuccessful) {
                    _productList.value = response.body() ?: emptyList()
                } else {
                    _productList.value = emptyList()
                }
            } catch (e: Exception) {
                _productList.value = emptyList()
            }
        }
    }

    private val _selectedProduct = MutableLiveData<Product>()
    val selectedProduct: LiveData<Product> = _selectedProduct

    // 상품 id 로 상품조회
    fun getProduct(productId: Int) {
        viewModelScope.launch {
            try {
                val product = productService.getProduct(productId)
                _selectedProduct.value = product
            } catch (e: Exception) {
                Log.e("ViewModel", "상품 정보 조회 실패: ${e.message}")
            }
        }
    }

    // 검색 기능
    fun searchProducts(keyword: String) {
        viewModelScope.launch {
            try {
                val response = productService.searchProducts(keyword)
                if (response.isSuccessful) {
                    _productList.value = response.body() ?: emptyList()
                } else {
                    _productList.value = emptyList()
                }
            } catch (e: Exception) {
                _productList.value = emptyList()
            }
        }
    }

    // 카테고리에서 클릭해서 검색
    fun searchByCategory(category: String) {
        viewModelScope.launch {
            try {
                val response = if (category == "전체") {
                    productService.getAllProducts()
                } else {
                    productService.getProductsByCategory(category)
                }

                if (response.isSuccessful) {
                    _productList.value = response.body() ?: emptyList()
                } else {
                    _productList.value = emptyList()
                }
            } catch (e: Exception) {
                _productList.value = emptyList()
            }
        }
    }

    fun getAllProductComments() {
        viewModelScope.launch {
            try {
                val response = commentService.getAllComments()
                if (response.isSuccessful) {
                    _productComments.value = response.body() ?: emptyList()
                } else {
                    _productComments.value = emptyList()
                }
            } catch (e: Exception) {
                _productComments.value = emptyList()
            }
        }
    }




    //////////////////////////////////// shopping cart ////////////////////////////////////
    private val _cartList = MutableLiveData<List<Product>>(mutableListOf())
    val cartList: LiveData<List<Product>> = _cartList

    // 상품담기
    fun addToCart(product: Product) {
        val current = _cartList.value?.toMutableList() ?: mutableListOf()

        val index = current.indexOfFirst { it.id == product.id }
        if (index != -1) {
            val updated = current[index].copy(
                count = current[index].count + 1,
                comment = current[index].comment ?: emptyList()
            )
            current[index] = updated
        } else {
            current.add(product.copy(count = 1, comment = product.comment ?: emptyList()))
        }

        _cartList.value = current.toList()
    }

    fun increaseCount(product: Product) {
        updateCount(product, product.count + 1)
    }

    fun decreaseCount(product: Product) {
        if (product.count > 1) updateCount(product, product.count - 1)
    }

    fun removeItem(product: Product) {
        _cartList.value = _cartList.value?.filter { it.id != product.id }
    }

    private fun updateCount(product: Product, newCount: Int) {
        _cartList.value = _cartList.value?.map {
            if (it.id == product.id) it.copy(count = newCount) else it
        }
    }



    //////////////////////////////////// order ////////////////////////////////////

    // 장바구니 상품 주문
    fun submitOrder(userId: String, storeName: String) {
        viewModelScope.launch {
            val cart = cartList.value ?: return@launch

            val details = cart.map {
                OrderDetail(
                    productId = it.id,
                    quantity = it.count
                ).apply {
                    unitPrice = it.price
                    productName = it.name
                    img = it.img
                }
            }

            val orderRequest = OrderRequest(userId, storeName, details)

            try {
                val response = orderService.submitOrder(orderRequest)
                if (response.isSuccessful) {
                    _cartList.value = emptyList()
                } else {
                    Log.e("Order", "주문 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Order", "네트워크 오류: ${e.message}")
            }
        }
    }

    private val _orderList = MutableLiveData<List<Order>>()
    val orderList: LiveData<List<Order>> get() = _orderList

    // user 주문 내역
    fun getUserOrders(userId: String, months: Int? = null) {
        viewModelScope.launch {
            try {
                val response = if (months != null) {
                    orderService.getUserOrdersByPeriod(userId, months)
                } else {
                    orderService.getUserOrders(userId)
                }

                if (response.isSuccessful) {
                    _orderList.value = response.body() ?: emptyList()
                } else {
                    _orderList.value = emptyList()
                    Log.e("OrderViewModel", "오류 코드: ${response.code()}")
                }
            } catch (e: Exception) {
                _orderList.value = emptyList()
                Log.e("OrderViewModel", "네트워크 오류: ${e.message}")
            }
        }
    }

    //////////////////////////////////// comment ////////////////////////////////////

    private val _userComments = MutableLiveData<List<ProductComment>>()
    val userComments: LiveData<List<ProductComment>> get() = _userComments

    // 사용자 id로 나의 리뷰 조회
    fun getUserComments(userId: String) {
        viewModelScope.launch {
            try {
                val response = commentService.getCommentsByUser(userId)
                if (response.isSuccessful) {
                    _userComments.value = response.body() ?: emptyList()
                } else {
                    _userComments.value = emptyList()
                }
            } catch (e: Exception) {
                _userComments.value = emptyList()
            }
        }
    }

    private val _productComments = MutableLiveData<List<ProductComment>>()
    val productComments: LiveData<List<ProductComment>> = _productComments

    // 상품 리뷰 가져오기
    fun getProductComments(productId: Int) {
        viewModelScope.launch {
            try {
                val response = commentService.getComments(productId)
                _productComments.value = response
            } catch (e: Exception) {
                _productComments.value = emptyList()
            }
        }
    }

    // 리뷰 등록
    private val _reviewResult = MutableLiveData<Result<Unit>>()
    val reviewResult: LiveData<Result<Unit>> = _reviewResult

    fun postReview(comment: ProductComment) {
        viewModelScope.launch {
            try {
                val response = commentService.postComment(comment)
                if (response.isSuccessful) {
                    _reviewResult.value = Result.success(Unit)
                } else {
                    _reviewResult.value = Result.failure(Exception("등록 실패: ${response.code()}"))
                }
            } catch (e: Exception) {
                _reviewResult.value = Result.failure(e)
            }
        }
    }

    fun deleteComment(commentId: Int, userId: String) {
        viewModelScope.launch {
            try {
                val response = commentService.deleteComment(commentId)
                if (response.isSuccessful) {
                    getUserComments(userId)
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "리뷰 삭제 실패", e)
            }
        }
    }

    fun updateComment(updatedComment: ProductComment, userId: String) {
        viewModelScope.launch {
            try {
                val response = commentService.updateComment(updatedComment)
                if (response.isSuccessful) {
                    getUserComments(userId)
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "리뷰 수정 실패", e)
            }
        }
    }



    //////////////////////////////////// map ////////////////////////////////////
    private val _stores = MutableLiveData<List<Store>>()
    val stores: LiveData<List<Store>> get() = _stores

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    // 진행중
    fun getAllStore() {
        viewModelScope.launch {
            try {
                val response = storeService.getAllStore()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _stores.value = it
                        _error.value = null
                    } ?: run {
                        _error.value = "서버에서 데이터를 가져오지 못했습니다."
                    }
                } else {
                    _error.value = "서버 오류: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "네트워크 오류: ${e.message}"
            }
        }
    }


    //////////////////////////////////// attendance ////////////////////////////////////

    private val _attendanceList = MutableLiveData<List<String>>()
    val attendanceList: LiveData<List<String>> get() = _attendanceList

    private val _attendanceCheckResult = MutableLiveData<Boolean>()
    val attendanceCheckResult: LiveData<Boolean> get() = _attendanceCheckResult

    // 출석 여부 확인
    fun getAttendance(userId: String) {
        viewModelScope.launch {
            try {
                val response = attendanceService.getHistory(userId)
                if (response.isSuccessful) {
                    _attendanceList.value = response.body() ?: emptyList()
                } else {
                    _attendanceList.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "출석 내역 불러오기 실패", e)
                _attendanceList.value = emptyList()
            }
        }
    }

    // 출석 체크
    fun checkAttendance(userId: String) {
        viewModelScope.launch {
            try {
                val response = attendanceService.check(userId)
                if (response.isSuccessful) {
                    // 서버에서 성공 반환 시 출석 내역을 다시 불러오기
                    getAttendance(userId)
                    _attendanceCheckResult.value = true
                } else {
                    _attendanceCheckResult.value = false
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "출석 체크 실패", e)
                _attendanceCheckResult.value = false
            }
        }
    }
}
