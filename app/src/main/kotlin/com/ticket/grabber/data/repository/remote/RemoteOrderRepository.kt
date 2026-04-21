package com.ticket.grabber.data.repository.remote

import com.ticket.grabber.data.api.TicketApi
import com.ticket.grabber.data.local.OrderDao
import com.ticket.grabber.data.model.*
import com.ticket.grabber.data.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

/**
 * 订单远程仓库实现
 */
class RemoteOrderRepository(
    private val api: TicketApi,
    private val dao: OrderDao
) : OrderRepository {
    
    override suspend fun submitOrder(request: SubmitOrderRequest): String? {
        return try {
            val apiResponse = api.submitOrder(
                trainNo = request.trainNo,
                fromStation = request.fromStation,
                toStation = request.toStation,
                seatType = request.seatType,
                passengers = request.passengers.joinToString(",") { "${it.name}:${it.idNo}:${it.seatType}" },
                ticketType = "1"
            )
            parseOrderNo(apiResponse.string())
        } catch (e: Exception) {
            null
        }
    }
    
    override fun getOrderList(query: OrderQuery): Flow<List<Order>> {
        return if (query.status != null) {
            dao.getByStatus(query.status)
        } else {
            dao.getAll()
        }
    }
    
    override suspend fun getOrderDetail(orderNo: String): Order? {
        return dao.getByOrderNo(orderNo)
    }
    
    override suspend fun cancelOrder(orderNo: String): Boolean {
        return try {
            api.cancelOrder(orderNo = orderNo)
            dao.update(dao.getByOrderNo(orderNo)?.copy(status = 2)!!)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun updateOrderStatus(orderNo: String, status: Int) {
        dao.update(dao.getByOrderNo(orderNo)?.copy(status = status)!!)
    }
    
    private fun parseOrderNo(json: String): String? {
        // TODO: 解析返回的订单号
        return null
    }
}
