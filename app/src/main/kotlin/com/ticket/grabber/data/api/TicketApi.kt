package com.ticket.grabber.data.api

import com.ticket.grabber.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * 12306 API接口定义
 * 包含所有与12306后端服务交互的API方法
 */
interface TicketApi {

    // ==================== 车票查询相关 ====================
    
    /**
     * 查询车次列表
     * GET /api/v1/trains/query
     */
    @GET("api/v1/trains/query")
    suspend fun queryTrains(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("date") date: String,
        @Query("station_type") stationType: String = "lik"
    ): ResponseBody

    /**
     * 查询车站信息
     * GET /api/v1/stations
     */
    @GET("api/v1/stations")
    suspend fun getStations(): ResponseBody

    /**
     * 查询车站名称和code映射
     * GET /api/v1/stations/map
     */
    @GET("api/v1/stations/map")
    suspend fun getStationNameMap(): ResponseBody

    // ==================== 订单相关 ====================
    
    /**
     * 提交订单
     * POST /api/v1/orders/submit
     */
    @FormUrlEncoded
    @POST("api/v1/orders/submit")
    suspend fun submitOrder(
        @Field("train_no") trainNo: String,
        @Field("from_station") fromStation: String,
        @Field("to_station") toStation: String,
        @Field("seat_type") seatType: String,
        @Field("passengers") passengers: String,
        @Field("ticket_type") ticketType: String = "1"
    ): ResponseBody

    /**
     * 查询订单列表
     * GET /api/v1/orders/list
     */
    @GET("api/v1/orders/list")
    suspend fun getOrders(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20,
        @Query("status") status: String? = null
    ): ResponseBody

    /**
     * 查询订单详情
     * GET /api/v1/orders/{orderNo}
     */
    @GET("api/v1/orders/{orderNo}")
    suspend fun getOrderDetail(
        @Path("orderNo") orderNo: String
    ): ResponseBody

    /**
     * 取消订单
     * POST /api/v1/orders/{orderNo}/cancel
     */
    @FormUrlEncoded
    @POST("api/v1/orders/{orderNo}/cancel")
    suspend fun cancelOrder(
        @Path("orderNo") orderNo: String,
        @Field("reason") reason: String? = null
    ): ResponseBody

    // ==================== 乘客相关 ====================
    
    /**
     * 获取乘客列表
     * GET /api/v1/passengers
     */
    @GET("api/v1/passengers")
    suspend fun getPassengers(): ResponseBody

    /**
     * 添加乘客
     * POST /api/v1/passengers
     */
    @FormUrlEncoded
    @POST("api/v1/passengers")
    suspend fun addPassenger(
        @Field("name") name: String,
        @Field("id_type") idType: String = "1",
        @Field("id_no") idNo: String,
        @Field("phone") phone: String,
        @Field("is_default") isDefault: Boolean = false
    ): ResponseBody

    /**
     * 删除乘客
     * DELETE /api/v1/passengers/{id}
     */
    @DELETE("api/v1/passengers/{id}")
    suspend fun deletePassenger(
        @Path("id") id: Long
    ): ResponseBody

    /**
     * 更新乘客信息
     * PUT /api/v1/passengers/{id}
     */
    @FormUrlEncoded
    @PUT("api/v1/passengers/{id}")
    suspend fun updatePassenger(
        @Path("id") id: Long,
        @Field("name") name: String,
        @Field("id_type") idType: String = "1",
        @Field("id_no") idNo: String,
        @Field("phone") phone: String
    ): ResponseBody

    // ==================== 账户相关 ====================
    
    /**
     * 用户登录
     * POST /api/v1/auth/login
     */
    @FormUrlEncoded
    @POST("api/v1/auth/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("captcha") captcha: String? = null,
        @Field("uuid") uuid: String? = null
    ): ResponseBody

    /**
     * 获取验证码
     * GET /api/v1/auth/captcha
     */
    @GET("api/v1/auth/captcha")
    suspend fun getCaptchaImage(): ResponseBody

    /**
     * 用户登出
     * POST /api/v1/auth/logout
     */
    @POST("api/v1/auth/logout")
    suspend fun logout(): ResponseBody

    /**
     * 检查登录状态
     * GET /api/v1/auth/status
     */
    @GET("api/v1/auth/status")
    suspend fun checkAuthStatus(): ResponseBody

    // ==================== 余票查询 ====================
    
    /**
     * 查询余票
     * GET /api/v1/tickets/availability
     */
    @GET("api/v1/tickets/availability")
    suspend fun checkTicketAvailability(
        @Query("train_no") trainNo: String,
        @Query("from_station") fromStation: String,
        @Query("to_station") toStation: String,
        @Query("date") date: String
    ): ResponseBody

    // ==================== 支付相关 ====================
    
    /**
     * 创建支付订单
     * POST /api/v1/payments
     */
    @FormUrlEncoded
    @POST("api/v1/payments")
    suspend fun createPayment(
        @Field("order_no") orderNo: String,
        @Field("amount") amount: Double,
        @Field("payment_method") paymentMethod: String
    ): ResponseBody

    /**
     * 查询支付状态
     * GET /api/v1/payments/{paymentNo}
     */
    @GET("api/v1/payments/{paymentNo}")
    suspend fun getPaymentStatus(
        @Path("paymentNo") paymentNo: String
    ): ResponseBody

    // ==================== 配置相关 ====================
    
    /**
     * 获取应用配置
     * GET /api/v1/config
     */
    @GET("api/v1/config")
    suspend fun getAppConfig(): ResponseBody

    /**
     * 上传反馈
     * POST /api/v1/feedback
     */
    @Multipart
    @POST("api/v1/feedback")
    suspend fun submitFeedback(
        @Part("content") content: RequestBody,
        @Part("contact") contact: RequestBody?,
        @Part image: MultipartBody.Part?
    ): ResponseBody
}
