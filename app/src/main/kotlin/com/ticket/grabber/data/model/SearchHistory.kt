package com.ticket.grabber.data.model

import androidx.room.*
import java.time.LocalDateTime

/**
 * 搜索历史实体
 */
@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = System.currentTimeMillis(),
    val fromStation: String,
    val fromStationName: String,
    val toStation: String,
    val toStationName: String,
    val departureDate: String,
    val departureTime: String = "08:00",
    val seatType: String = "2",
    val queryTime: Long = System.currentTimeMillis()
) {
    fun toTrainQuery(): TrainQuery {
        return TrainQuery(
            fromStation = fromStation,
            toStation = toStation,
            departureDate = departureDate
        )
    }
}

/**
 * 搜索历史查询参数
 */
data class SearchHistoryQuery(
    val limit: Int = 20,
    val offset: Int = 0
)

/**
 * 简化的搜索历史记录
 */
data class SimpleSearchHistory(
    val id: Long,
    val fromStation: String,
    val fromStationName: String,
    val toStation: String,
    val toStationName: String,
    val departureDate: String,
    val queryTime: Long
) {
    companion object {
        fun from(history: SearchHistory): SimpleSearchHistory {
            return SimpleSearchHistory(
                id = history.id,
                fromStation = history.fromStation,
                fromStationName = history.fromStationName,
                toStation = history.toStation,
                toStationName = history.toStationName,
                departureDate = history.departureDate,
                queryTime = history.queryTime
            )
        }
    }
}
