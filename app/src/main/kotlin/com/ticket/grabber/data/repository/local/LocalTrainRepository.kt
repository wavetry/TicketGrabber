package com.ticket.grabber.data.repository.local

import com.ticket.grabber.data.local.TrainDao
import com.ticket.grabber.data.model.Train
import com.ticket.grabber.data.model.TrainQuery
import com.ticket.grabber.data.repository.TrainRepository

/**
 * 列车本地仓库实现
 */
class LocalTrainRepository(
    private val dao: TrainDao
) : TrainRepository {
    
    override suspend fun queryTrains(query: TrainQuery): List<Train> {
        return dao.getAll()
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
}
