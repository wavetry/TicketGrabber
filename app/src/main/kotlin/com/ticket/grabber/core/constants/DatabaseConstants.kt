package com.ticket.grabber.core.constants

/**
 * 数据库常量定义
 */
object DatabaseConstants {
    
    // 列名
    const val COL_ID = "id"
    const val COL_CODE = "code"
    const val COL_NAME = "name"
    const val COL_Pinyin = "pinyin"
    const val COL_TYPE = "type"
    const val COL_STATUS = "status"
    const val COL_TIME = "time"
    const val COL_PRICE = "price"
    const val COL_CREATE_TIME = "create_time"
    const val COL_UPDATE_TIME = "update_time"
    const val COL_IS_DEFAULT = "is_default"
    
    // 表名
    const val TABLE_USERS = "users"
    const val TABLE_STATIONS = "stations"
    const val TABLE_TRAINS = "trains"
    const val TABLE_PASSENGERS = "passengers"
    const val TABLE_ORDERS = "orders"
    const val TABLE_PAYMENTS = "payments"
    
    // 数据库版本
    const val DB_VERSION = 1
}
