package com.ticket.grabber.data.repository.local

import com.ticket.grabber.data.local.OrderDao
import com.ticket.grabber.data.model.Order
import com.ticket.grabber.data.model.OrderQuery
import com.ticket.grabber.data.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

/**
 * 订单本地仓库实现
 */
class LocalOrderRepository(
    private val dao: OrderDao
) : OrderRepository {
    
    override suspend fun submitOrder(request: com.ticket.grabber.data.model.SubmitOrderRequest): String? {
        val order = Order(
            orderNo = "LOCAL_${System.currentTimeMillis()}",
            trainNo = request.trainNo,
            fromStation = request.fromStation,
            toStation = request.toStation,
            departureDate = request.departureDate,
            passengers = "", // JSON格式
            seats = "", // JSON格式
            totalPrice = 0.0,
            status = 0
        )
        dao.insert(order)
        return order.orderNo
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
        val order = dao.getByOrderNo(orderNo) ?: return false
        dao.update(order.copy(status = 2))
        return true
    }
    
    override suspend fun updateOrderStatus(orderNo: String, status: Int) {
        dao.update(dao.getByOrderNo(orderNo)?.copy(status = status)!!)
    }
}
