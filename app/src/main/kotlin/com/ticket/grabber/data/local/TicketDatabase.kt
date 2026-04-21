package com.ticket.grabber.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ticket.grabber.data.model.*

/**
 * 12306数据库
 * 包含所有本地数据表
 */
@Database(
    entities = [
        Station::class,
        Train::class,
        Passenger::class,
        Order::class,
        Payment::class,
        GrabTask::class,
        GrabResult::class,
        SearchHistory::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TicketDatabase : RoomDatabase() {
    
    abstract fun stationDao(): StationDao
    abstract fun trainDao(): TrainDao
    abstract fun passengerDao(): PassengerDao
    abstract fun orderDao(): OrderDao
    abstract fun paymentDao(): PaymentDao
    abstract fun grabTaskDao(): GrabTaskDao
    abstract fun grabResultDao(): GrabResultDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}