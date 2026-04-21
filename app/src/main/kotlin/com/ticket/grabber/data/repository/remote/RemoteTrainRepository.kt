package com.ticket.grabber.data.repository.remote

import com.ticket.grabber.data.api.TicketApi
import com.ticket.grabber.data.local.TrainDao
import com.ticket.grabber.data.model.Train
import com.ticket.grabber.data.model.TrainQuery
import com.ticket.grabber.data.repository.SearchRepository
import com.ticket.grabber.data.repository.TrainWithAvailability
import com.ticket.grabber.data.repository.TrainRepository
import kotlinx.coroutines.flow.Flow

/**
 * 列车远程仓库实现
 */
class RemoteTrainRepository(
    private val api: TicketApi,
    private val dao: TrainDao
) : TrainRepository {
    
    override suspend fun queryTrains(query: TrainQuery): List<Train> {
        return try {
            val apiResponse = api.queryTrains(
                from = query.fromStation,
                to = query.toStation,
                date = query.departureDate
            )
            val trains = parseTrains(apiResponse.string())
            dao.insertAll(trains)
            trains
        } catch (e: Exception) {
            // 返回本地缓存
            dao.getAll()
        }
    }
    
    override suspend fun queryTrainsWithAvailability(query: TrainQuery): List<TrainWithAvailability> {
        val trains = queryTrains(query)
        return trains.map { train ->
            TrainWithAvailability(train = train)
        }
    }
    
    override suspend fun getTrainDetail(trainNo: String): Train? {
        return dao.getByTrainNo(trainNo)
    }
    
    override suspend fun saveTrain(train: Train) {
        dao.insert(train)
    }
    
    override suspend fun saveTrains(trains: List<Train>) {
        dao.insertAll(trains)
    }
    
    private fun parseTrains(json: String): List<Train> {
        val trains = mutableListOf<Train>()
        
        try {
            // 尝试解析JSON
            val arr = if (json.startsWith("\"")) json.removePrefix("\"").removeSuffix("\"") else json
            val reader = android.util.JsonReader(arr.reader())
            reader.beginArray()
            
            while (reader.hasNext()) {
                reader.beginObject()
                var trainNo: String? = null
                var trainCode: String? = null
                var fromStation: String? = null
                var toStation: String? = null
                var departureTime: String? = null
                var arrivalTime: String? = null
                var duration: Long? = null
                
                while (reader.hasNext()) {
                    val name = reader.nextName()
                    when (name) {
                        "train_no" -> trainNo = reader.nextString()
                        "station_train_code" -> trainCode = reader.nextString()
                        "from_station" -> fromStation = reader.nextString()
                        "to_station" -> toStation = reader.nextString()
                        "departure_time" -> departureTime = reader.nextString()
                        "arrival_time" -> arrivalTime = reader.nextString()
                        "duration" -> duration = reader.nextLong()
                        else -> reader.skipValue()
                    }
                }
                reader.endObject()
                
                if (trainNo != null && trainCode != null) {
                    trains.add(Train(
                        trainNo = trainNo,
                        trainCode = trainCode,
                        fromStation = fromStation ?: "",
                        toStation = toStation ?: "",
                        departureTime = departureTime ?: "",
                        arrivalTime = arrivalTime ?: "",
                        duration = duration ?: 0L
                    ))
                }
            }
            reader.endArray()
        } catch (e: Exception) {
            // 解析失败返回空列表
            e.printStackTrace()
        }
        
        return trains
    }
}
