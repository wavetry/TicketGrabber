package com.ticket.grabber.data.model

import androidx.room.*

/**
 * 抢票任务实体类
 * 用于管理抢票任务
 */
@Entity(tableName = "grab_tasks")
data class GrabTask(
    @PrimaryKey val taskId: String,
    val trainNo: String,
    val trainCode: String,
    val fromStation: String,
    val toStation: String,
    val departureDate: String,
    val seatTypes: String, // JSON格式存储席别列表 ["M", "O", "1"]
    val passengers: String, // JSON格式存储乘客ID列表
    val status: Int = 0, // 0:待启动, 1:抢票中, 2:已抢到, 3:已失败, 4:已取消
    val priority: Int = 0, // 优先级
    val retryCount: Int = 0, // 重试次数
    val maxRetryCount: Int = 100, // 最大重试次数
    val queryInterval: Long = 3000, // 查询间隔(毫秒)
    val orderNo: String? = null, // 抢到后的订单号
    val errorMessage: String? = null, // 错误信息
    val createTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis(),
    val startTime: Long? = null, // 开始抢票时间
    val endTime: Long? = null // 结束时间
)

/**
 * 抢票结果实体类
 * 用于记录抢票结果
 */
@Entity(tableName = "grab_results")
data class GrabResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val taskId: String,
    val success: Boolean,
    val orderNo: String? = null,
    val message: String? = null,
    val seatType: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * 抢票任务状态
 */
enum class GrabTaskStatus(val code: Int, val name: String) {
    PENDING(0, "待启动"),
    RUNNING(1, "抢票中"),
    SUCCESS(2, "已抢到"),
    FAILED(3, "已失败"),
    CANCELLED(4, "已取消")
}

/**
 * 创建抢票任务请求
 */
data class CreateGrabTaskRequest(
    val trainNo: String,
    val trainCode: String,
    val fromStation: String,
    val toStation: String,
    val departureDate: String,
    val seatTypes: List<String>, // 席别代码列表，按优先级排序
    val passengerIds: List<Long>, // 乘客ID列表
    val priority: Int = 0,
    val maxRetryCount: Int = 100,
    val queryInterval: Long = 3000
)

/**
 * 抢票任务查询参数
 */
data class GrabTaskQuery(
    val status: Int? = null,
    val page: Int = 1,
    val size: Int = 20
)

/**
 * 余票 availability 信息
 */
data class TicketAvailability(
    val hasTickets: Boolean,
    val seatAvailability: Map<String, Boolean>, // 席别 -> 是否有票
    val remainingCount: Map<String, Int>? = null // 席别 -> 剩余数量
)
