package com.ticket.grabber.core.database

import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Companion.openHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.ticket.grabber.data.local.StationDao
import com.ticket.grabber.data.local.TrainDao
import com.ticket.grabber.data.local.PassengerDao
import com.ticket.grabber.data.local.OrderDao
import com.ticket.grabber.data.local.PaymentDao
import com.ticket.grabber.data.local.TicketDatabase
import com.ticket.grabber.data.local.Converters

/**
 * 数据库辅助类
 * 提供数据库操作的工具方法
 */
object DatabaseHelper {
    
    /**
     * 创建数据库配置
     */
    fun createDatabaseConfiguration(): DatabaseConfiguration {
        return DatabaseConfiguration(
            context = TODO("Provide context"),
            name = "ticket_grabber.db",
            openHelperFactory = FrameworkSQLiteOpenHelperFactory(),
            foreignKeyConstraints = false,
            enableWriteAheadLogging = false,
            queryExecutor = null,
            migrationContainer = null
        )
    }
    
    /**
     * 获取所有DAO
     */
    fun getAllDaos(database: TicketDatabase): DatabaseDaos {
        return DatabaseDaos(
            stationDao = database.stationDao(),
            trainDao = database.trainDao(),
            passengerDao = database.passengerDao(),
            orderDao = database.orderDao(),
            paymentDao = database.paymentDao()
        )
    }
}

/**
 * DAO容器
 * 方便一次性获取所有DAO
 */
data class DatabaseDaos(
    val stationDao: StationDao,
    val trainDao: TrainDao,
    val passengerDao: PassengerDao,
    val orderDao: OrderDao,
    val paymentDao: PaymentDao
)
