package com.ticket.grabber.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 订单实体类
 * 用于本地缓存订单信息
 */
@Entity(tableName = "orders")
data class Order(
    @PrimaryKey val orderNo: String,
    val trainNo: String,
    val trainCode: String,
    val fromStation: String,
    val toStation: String,
    val departureDate: String,
    val departureTime: String,
    val arrivalTime: String,
    val passengers: String, // JSON格式存储乘客信息
    val seats: String, // JSON格式存储座位信息
    val totalPrice: Double,
    val status: Int = 0, // 0:待支付, 1:已支付, 2:已取消, 3:已出行, 4:已退票
    val paymentNo: String? = null,
    val paymentTime: Long? = null,
    val createTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis()
)

/**
 * 订单状态
 */
enum class OrderStatus(val code: Int, val name: String) {
    PENDING_PAY(0, "待支付"),
    PAID(1, "已支付"),
    CANCELLED(2, "已取消"),
    COMPLETED(3, "已出行"),
    REFUNDED(4, "已退票")
}

/**
 * 订单查询参数
 */
data class OrderQuery(
    val page: Int = 1,
    val size: Int = 20,
    val status: Int? = null
)

/**
 * 提交订单请求
 */
data class SubmitOrderRequest(
    val trainNo: String,
    val fromStation: String,
    val toStation: String,
    val departureDate: String,
    val passengers: List<PassengerInfo>,
    val seatType: String = "2" // 硬座
)

/**
 * 乘客信息
 */
data class PassengerInfo(
    val name: String,
    val idType: Int = 1,
    val idNo: String,
    val seatType: String = "2"
)
