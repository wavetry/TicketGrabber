package com.ticket.grabber.data.repository.remote

import com.ticket.grabber.data.api.TicketApi
import com.ticket.grabber.data.local.PassengerDao
import com.ticket.grabber.data.model.AddPassengerRequest
import com.ticket.grabber.data.model.Passenger
import com.ticket.grabber.data.model.UpdatePassengerRequest
import com.ticket.grabber.data.repository.PassengerRepository

/**
 * 乘客远程仓库实现
 */
class RemotePassengerRepository(
    private val api: TicketApi,
    private val dao: PassengerDao
) : PassengerRepository {
    
    override suspend fun getAllPassengers(): List<Passenger> {
        return try {
            val apiResponse = api.getPassengers()
            val passengers = parsePassengers(apiResponse.string())
            dao.insertAll(passengers)
            passengers
        } catch (e: Exception) {
            dao.getAll()
        }
    }
    
    override suspend fun getDefaultPassenger(): Passenger? {
        return dao.getDefaultPassenger()
    }
    
    override suspend fun addPassenger(request: AddPassengerRequest): Long {
        return try {
            val apiResponse = api.addPassenger(
                name = request.name,
                idType = request.idType,
                idNo = request.idNo,
                phone = request.phone,
                isDefault = request.isDefault
            )
            val id = parsePassengerId(apiResponse.string())
            if (id > 0) {
                dao.insert(
                    Passenger(
                        id = id,
                        name = request.name,
                        idType = request.idType,
                        idNo = request.idNo,
                        phone = request.phone,
                        isDefault = request.isDefault
                    )
                )
            }
            id
        } catch (e: Exception) {
            // 本地添加
            val passenger = Passenger(
                id = System.currentTimeMillis(),
                name = request.name,
                idType = request.idType,
                idNo = request.idNo,
                phone = request.phone,
                isDefault = request.isDefault
            )
            dao.insert(passenger)
        }
    }
    
    override suspend fun updatePassenger(id: Long, request: UpdatePassengerRequest) {
        try {
            api.updatePassenger(
                id = id,
                name = request.name,
                idType = request.idType,
                idNo = request.idNo,
                phone = request.phone
            )
            dao.update(
                Passenger(
                    id = id,
                    name = request.name,
                    idType = request.idType,
                    idNo = request.idNo,
                    phone = request.phone
                )
            )
        } catch (e: Exception) {
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
    }
    
    override suspend fun deletePassenger(id: Long) {
        try {
            api.deletePassenger(id = id)
            dao.deleteById(id)
        } catch (e: Exception) {
            dao.deleteById(id)
        }
    }
    
    override suspend fun setDefaultPassenger(id: Long) {
        dao.clearAllDefaults()
        dao.setDefault(id)
    }
    
    private fun parsePassengers(json: String): List<Passenger> {
        // TODO: 解析JSON响应
        return emptyList()
    }
    
    private fun parsePassengerId(json: String): Long {
        // TODO: 解析返回的乘客ID
        return 0L
    }
}
