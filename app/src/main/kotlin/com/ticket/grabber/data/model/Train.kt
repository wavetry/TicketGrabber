package com.ticket.grabber.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 列车实体类
 * 用于缓存列车基本信息
 */
@Entity(tableName = "trains")
data class Train(
    @PrimaryKey val trainNo: String,
    val trainCode: String,
    val fromStation: String,
    val toStation: String,
    val departureTime: String,
    val arrivalTime: String,
    val duration: Long,
    val departureStationName: String = "",
    val arrivalStationName: String = "",
    val stopCount: Int = 0,
    val createTime: Long = System.currentTimeMillis()
)

/**
 * 列车查询参数
 */
data class TrainQuery(
    val fromStation: String,
    val toStation: String,
    val departureDate: String,
    val trainType: String? = null
)

/**
 * 列车座位类型
 */
enum class SeatType(val code: String, val name: String) {
    SOFT_SEAT("1", "软座"),
    HARD_SEAT("2", "硬座"),
    SOFT_BED("3", "软卧"),
    HARD_BED("4", "硬卧"),
    BUSINESS_SEAT("9", "商务座"),
    FIRST_CLASS_SEAT("M", "一等座"),
    SECOND_CLASS_SEAT("O", "二等座"),
    ADVANCED_SOFT_BED("6", "高级软卧")
}
