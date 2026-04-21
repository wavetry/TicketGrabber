package com.ticket.grabber.data.repository.local

import com.ticket.grabber.data.local.PassengerDao
import com.ticket.grabber.data.model.AddPassengerRequest
import com.ticket.grabber.data.model.Passenger
import com.ticket.grabber.data.model.UpdatePassengerRequest
import com.ticket.grabber.data.repository.PassengerRepository

/**
 * 乘客本地仓库实现
 */
class LocalPassengerRepository(
    private val dao: PassengerDao
) : PassengerRepository {
    
    override suspend fun getAllPassengers(): List<Passenger> {
        return dao.getAll()
    }
    
    override suspend fun getDefaultPassenger(): Passenger? {
        return dao.getDefaultPassenger()
    }
    
    override suspend fun addPassenger(request: AddPassengerRequest): Long {
        val passenger = Passenger(
            id = System.currentTimeMillis(),
            name = request.name,
            idType = request.idType,
            idNo = request.idNo,
            phone = request.phone,
            isDefault = request.isDefault
        )
        return dao.insert(passenger)
    }
    
    override suspend fun updatePassenger(id: Long, request: UpdatePassengerRequest) {
        dao.update(
            Passenger(
                id = id,
                name = request.name,
                idType = request.idType,
                idNo = request.idNo,
                phone = request.phone
            )
        )
    }
    
    override suspend fun deletePassenger(id: Long) {
        dao.deleteById(id)
    }
    
    override suspend fun setDefaultPassenger(id: Long) {
        dao.clearAllDefaults()
        dao.setDefault(id)
    }
}
