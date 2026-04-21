package com.ticket.grabber.data.repository.remote

import com.ticket.grabber.data.api.TicketApi
import com.ticket.grabber.data.local.*
import com.ticket.grabber.data.model.*
import com.ticket.grabber.data.repository.*
import kotlinx.coroutines.flow.Flow

/**
 * 远程车票查询仓库实现（组合所有远程仓库）
 */
class RemoteTicketRepository(
    private val api: TicketApi
) : TicketRepository {
    
    override suspend fun checkTicketAvailability(
        trainNo: String,
        fromStation: String,
        toStation: String,
        departureDate: String
    ): Result<TicketAvailability> {
        return try {
            val availability = api.checkTicketAvailability(
                trainNo = trainNo,
                fromStation = fromStation,
                toStation = toStation,
                date = departureDate
            )
            val json = availability.string()
            // TODO: 解析余票JSON
            Result.success(
                TicketAvailability(
                    hasTickets = true,
                    seatAvailability = mapOf(
                        "M" to true,
                        "O" to true,
                        "2" to true
                    )
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun submitOrder(
        trainNo: String,
        fromStation: String,
        toStation: String,
        departureDate: String,
        passengers: List<PassengerInfo>,
        seatType: String
    ): Result<Order> {
        return try {
            val response = api.submitOrder(
                trainNo = trainNo,
                fromStation = fromStation,
                toStation = toStation,
                seatType = seatType,
                passengers = passengers.joinToString(",") { "${it.name}:${it.idNo}:${it.seatType}" },
                ticketType = "1"
            )
            val json = response.string()
            // TODO: 解析订单JSON
            Result.success(
                Order(
                    orderNo = "ORDER_${System.currentTimeMillis()}",
                    trainNo = trainNo,
                    fromStation = fromStation,
                    toStation = toStation,
                    departureDate = departureDate,
                    passengers = passengers.toString(),
                    seats = "",
                    status = OrderStatus.PENDING_PAY.code
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
