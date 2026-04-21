package com.ticket.grabber.data.repository

import com.ticket.grabber.data.api.TicketApi
import com.ticket.grabber.data.local.SearchHistoryDao
import com.ticket.grabber.data.local.TrainDao
import com.ticket.grabber.data.model.SearchHistory
import com.ticket.grabber.data.model.Train
import com.ticket.grabber.data.model.TrainQuery
import kotlinx.coroutines.flow.Flow

/**
 * 搜索查询仓库实现
 */
class SearchRepositoryImpl(
    private val api: TicketApi,
    private val trainDao: TrainDao,
    private val searchHistoryDao: SearchHistoryDao
) : SearchRepository {
    
    override suspend fun queryTrains(query: TrainQuery): List<Train> {
        return try {
            val response = api.queryTrains(
                from = query.fromStation,
                to = query.toStation,
                date = query.departureDate
            )
            val trains = parseTrains(response.string())
            trainDao.insertAll(trains)
            trains
        } catch (e: Exception) {
            // 返回本地缓存
            trainDao.getAll()
        }
    }
    
    override suspend fun queryTrainsWithAvailability(query: TrainQuery): List<TrainWithAvailability> {
        val trains = queryTrains(query)
        
        return trains.map { train ->
            // 查询余票信息（实际应该调用API，这里简化处理）
            val availability = checkAvailability(train.trainNo, query.fromStation, query.toStation, query.departureDate)
            TrainWithAvailability(
                train = train,
                businessSeat = availability.businessSeat,
                firstClassSeat = availability.firstClassSeat,
                secondClassSeat = availability.secondClassSeat,
                hardSeat = availability.hardSeat,
                softBed = availability.softBed,
                hardBed = availability.hardBed,
                advancedSoftBed = availability.advancedSoftBed
            )
        }
    }
    
    override suspend fun saveSearchHistory(history: SearchHistory) {
        searchHistoryDao.insert(history)
        // 清理旧历史，保留最近50条
        searchHistoryDao.clearOldHistory(50)
    }
    
    override fun getSearchHistory(): Flow<List<SearchHistory>> {
        return searchHistoryDao.getHistory(SearchHistoryQuery())
    }
    
    override suspend fun deleteSearchHistory(history: SearchHistory) {
        searchHistoryDao.delete(history)
    }
    
    override suspend fun clearSearchHistory() {
        searchHistoryDao.clearAll()
    }
    
    override suspend fun getRecentSearchHistory(limit: Int = 10): List<SearchHistory> {
        return searchHistoryDao.getAllRecent(limit)
    }
    
    private suspend fun checkAvailability(
        trainNo: String,
        fromStation: String,
        toStation: String,
        date: String
    ): TrainWithAvailability {
        return try {
            val response = api.checkTicketAvailability(
                trainNo = trainNo,
                fromStation = fromStation,
                toStation = toStation,
                date = date
            )
            parseAvailability(response.string())
        } catch (e: Exception) {
            TrainWithAvailability(train = Train(trainNo = trainNo, trainCode = "", fromStation = fromStation, toStation = toStation, departureTime = "", arrivalTime = "", duration = 0))
        }
    }
    
    private fun parseTrains(json: String): List<Train> {
        val trains = mutableListOf<Train>()
        
        try {
            // 尝试解析JSON
            val arr = if (json.startsWith("\"")) json.removePrefix("\"").removeSuffix("\"") else json
            val JSONArray = android.util.JsonReader(arr.reader()).apply { beginArray() }
            
            while (JSONArray.hasNext()) {
                val obj = JSONArray.beginObject()
                val trainNo = obj.findString("train_no") ?: obj.findString("station_train_code")
                val trainCode = obj.findString("station_train_code") ?: obj.findString("train_no")
                val fromStation = obj.findString("from_station") ?: obj.findString("start_station")
                val toStation = obj.findString("to_station") ?: obj.findString("end_station")
                val departureTime = obj.findString("departure_time")
                val arrivalTime = obj.findString("arrival_time")
                val duration = obj.findLong("duration") ?: 0L
                
                if (trainNo != null && trainCode != null) {
                    trains.add(Train(
                        trainNo = trainNo,
                        trainCode = trainCode,
                        fromStation = fromStation ?: "",
                        toStation = toStation ?: "",
                        departureTime = departureTime ?: "",
                        arrivalTime = arrivalTime ?: "",
                        duration = duration
                    ))
                }
                JSONArray.endObject()
            }
            JSONArray.endArray()
        } catch (e: Exception) {
            // 解析失败返回空列表
            e.printStackTrace()
        }
        
        return trains
    }
    
    private fun parseAvailability(json: String): TrainWithAvailability {
        return try {
            val obj = if (json.startsWith("\"")) json.removePrefix("\"").removeSuffix("\"").let { android.util.JsonReader(it.reader()).beginObject() } else android.util.JsonReader(json.reader()).beginObject()
            
            val businessSeat = obj.findInt("business_seat") ?: 0
            val firstClassSeat = obj.findInt("first_class_seat") ?: 0
            val secondClassSeat = obj.findInt("second_class_seat") ?: 0
            val hardSeat = obj.findInt("hard_seat") ?: 0
            val softBed = obj.findInt("soft_bed") ?: 0
            val hardBed = obj.findInt("hard_bed") ?: 0
            val advancedSoftBed = obj.findInt("advanced_soft_bed") ?: 0
            
            obj.endObject()
            
            TrainWithAvailability(
                businessSeat = businessSeat,
                firstClassSeat = firstClassSeat,
                secondClassSeat = secondClassSeat,
                hardSeat = hardSeat,
                softBed = softBed,
                hardBed = hardBed,
                advancedSoftBed = advancedSoftBed
            )
        } catch (e: Exception) {
            TrainWithAvailability()
        }
    }
}

/**
 * JsonReader扩展函数
 */
private fun android.util.JsonReader.findString(name: String): String? {
    while (hasNext()) {
        val nextName = nextName()
        if (nextName == name) {
            return nextString()
        }
        skipValue()
    }
    return null
}

private fun android.util.JsonReader.findInt(name: String): Int? {
    while (hasNext()) {
        val nextName = nextName()
        if (nextName == name) {
            return nextInt()
        }
        skipValue()
    }
    return null
}

private fun android.util.JsonReader.findLong(name: String): Long? {
    while (hasNext()) {
        val nextName = nextName()
        if (nextName == name) {
            return nextLong()
        }
        skipValue()
    }
    return null
}
