package com.ticket.grabber.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 车站实体类
 * 用于本地缓存车站信息
 */
@Entity(tableName = "stations")
data class Station(
    @PrimaryKey val code: String,
    val name: String,
    val pinyin: String,
    val city: String,
    val province: String,
    val isCapital: Boolean = false,
    val createTime: Long = System.currentTimeMillis()
)

/**
 * 车站名称映射
 */
data class StationNameMap(
    val fullName: String,
    val code: String,
    val pinyin: String
)

/**
 * 站点查询参数
 */
data class StationQuery(
    val keyword: String,
    val limit: Int = 20
)
