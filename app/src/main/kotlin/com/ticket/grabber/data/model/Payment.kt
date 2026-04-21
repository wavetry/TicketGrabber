package com.ticket.grabber.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 支付订单实体类
 */
@Entity(tableName = "payments")
data class Payment(
    @PrimaryKey val paymentNo: String,
    val orderNo: String,
    val amount: Double,
    val paymentMethod: Int = 1, // 1:支付宝, 2:微信, 3:银联
    val status: Int = 0, // 0:待支付, 1:支付成功, 2:支付失败, 3:已退款
    val transactionNo: String? = null,
    val payTime: Long? = null,
    val createTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis()
)

/**
 * 支付方式
 */
enum class PaymentMethod(val code: Int, val name: String) {
    ALIPAY(1, "支付宝"),
    WECHAT(2, "微信"),
    UNIONPAY(3, "银联")
}

/**
 * 支付状态
 */
enum class PaymentStatus(val code: Int, val name: String) {
    PENDING(0, "待支付"),
    SUCCESS(1, "支付成功"),
    FAILED(2, "支付失败"),
    REFUNDED(3, "已退款")
}

/**
 * 创建支付请求
 */
data class CreatePaymentRequest(
    val orderNo: String,
    val amount: Double,
    val paymentMethod: Int = 1
)
