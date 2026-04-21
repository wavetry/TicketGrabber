package com.ticket.grabber.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 乘客实体类
 * 用于本地存储乘客信息
 */
@Entity(tableName = "passengers")
data class Passenger(
    @PrimaryKey val id: Long,
    val name: String,
    val idType: Int = 1, // 1:身份证, 2:护照, 3:港澳台通行证
    val idNo: String,
    val phone: String,
    val isDefault: Boolean = false,
    val createTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis()
)

/**
 * 乘客类型
 */
enum class IdType(val code: Int, val name: String) {
    ID_CARD(1, "身份证"),
    PASSPORT(2, "护照"),
   _MACAO_HK(3, "港澳台通行证")
)

/**
 * 添加乘客请求
 */
data class AddPassengerRequest(
    val name: String,
    val idType: Int = 1,
    val idNo: String,
    val phone: String,
    val isDefault: Boolean = false
)

/**
 * 更新乘客请求
 */
data class UpdatePassengerRequest(
    val name: String,
    val idType: Int = 1,
    val idNo: String,
    val phone: String
)
