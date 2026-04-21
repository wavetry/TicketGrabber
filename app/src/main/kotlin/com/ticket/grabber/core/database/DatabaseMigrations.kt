package com.ticket.grabber.core.database

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration

/**
 * 数据库迁移类
 * 处理数据库版本升级
 */
object DatabaseMigrations {
    
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // 添加新列
            db.execSQL("ALTER TABLE stations ADD COLUMN is_capital INTEGER DEFAULT 0")
        }
    }
    
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // 添加新表 - 搜索历史
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS search_history (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    from_station TEXT NOT NULL,
                    from_station_name TEXT NOT NULL,
                    to_station TEXT NOT NULL,
                    to_station_name TEXT NOT NULL,
                    departure_date TEXT NOT NULL,
                    departure_time TEXT DEFAULT '08:00',
                    seat_type TEXT DEFAULT '2',
                    query_time INTEGER NOT NULL
                )
            """.trimIndent())
        }
    }
    
    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // 添加新表 - 座位信息
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS seat_info (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    order_id INTEGER NOT NULL,
                    seat_no TEXT NOT NULL,
                    seat_type TEXT NOT NULL,
                    price REAL NOT NULL
                )
            """.trimIndent())
        }
    }
}
