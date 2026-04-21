package com.ticket.grabber.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Room类型转换器
 * 处理复杂对象的序列化和反序列化
 */
class Converters {
    
    private val gson = Gson()
    
    @TypeConverter
    fun fromPassengerList(value: List<com.ticket.grabber.data.model.PassengerInfo>?): String? {
        return value?.let { gson.toJson(it) }
    }
    
    @TypeConverter
    fun toPassengerList(value: String?): List<com.ticket.grabber.data.model.PassengerInfo>? {
        return value?.let { gson.fromJson(it, object : TypeToken<List<com.ticket.grabber.data.model.PassengerInfo>>() {}.type) }
    }
    
    @TypeConverter
    fun fromSeatList(value: List<com.ticket.grabber.data.model.SeatInfo>?): String? {
        return value?.let { gson.toJson(it) }
    }
    
    @TypeConverter
    fun toSeatList(value: String?): List<com.ticket.grabber.data.model.SeatInfo>? {
        return value?.let { gson.fromJson(it, object : TypeToken<List<com.ticket.grabber.data.model.SeatInfo>>() {}.type) }
    }
}
