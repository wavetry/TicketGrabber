package com.ticket.grabber.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ticket.grabber.data.model.AuthSession

/**
 * 认证会话数据库
 */
@Database(
    entities = [AuthSession::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AuthSessionDatabase : RoomDatabase() {
    
    abstract fun authDao(): AuthDao
}