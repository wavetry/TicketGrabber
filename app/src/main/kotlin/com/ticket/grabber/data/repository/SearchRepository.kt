package com.ticket.grabber.data.repository

import com.ticket.grabber.data.local.SearchHistoryDao
import com.ticket.grabber.data.model.SearchHistory
import com.ticket.grabber.data.model.Train
import com.ticket.grabber.data.model.TrainQuery
import kotlinx.coroutines.flow.Flow

/**
 * 搜索查询仓库接口
 * 处理车次查询相关的数据操作
 */
interface SearchRepository {
    
    /**
     * 查询车次列表
     */
    suspend fun queryTrains(query: TrainQuery): List<Train>
    
    /**
     * 查询车次（带余票信息）
     */
    suspend fun queryTrainsWithAvailability(query: TrainQuery): List<TrainWithAvailability>
    
    /**
     * 保存搜索历史
     */
    suspend fun saveSearchHistory(history: SearchHistory)
    
    /**
     * 获取搜索历史
     */
    fun getSearchHistory(): Flow<List<SearchHistory>>
    
    /**
     * 删除搜索历史
     */
    suspend fun deleteSearchHistory(history: SearchHistory)
    
    /**
     * 清空搜索历史
     */
    suspend fun clearSearchHistory()
    
    /**
     * 获取最近的搜索历史（前N条）
     */
    suspend fun getRecentSearchHistory(limit: Int = 10): List<SearchHistory>
}

/**
 * 带余票信息的列车
 */
data class TrainWithAvailability(
    val train: Train,
    val businessSeat: Int = 0, // 商务座
    val firstClassSeat: Int = 0, // 一等座
    val secondClassSeat: Int = 0, // 二等座
    val hardSeat: Int = 0, // 硬座
    val softBed: Int = 0, // 软卧
    val hardBed: Int = 0, // 硬卧
    val advancedSoftBed: Int = 0 // 高级软卧
) {
    fun toTrain(): Train = train
}

/**
 * 车次查询结果
 */
data class TrainSearchResult(
    val trains: List<TrainWithAvailability>,
    val totalCount: Int,
    val fromStation: String,
    val toStation: String,
    val departureDate: String
)
