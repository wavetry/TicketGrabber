package com.ticket.grabber.data.repository.remote

import com.ticket.grabber.data.api.TicketApi
import com.ticket.grabber.data.local.PaymentDao
import com.ticket.grabber.data.model.CreatePaymentRequest
import com.ticket.grabber.data.model.Payment
import com.ticket.grabber.data.repository.PaymentRepository

/**
 * 支付远程仓库实现
 */
class RemotePaymentRepository(
    private val api: TicketApi,
    private val dao: PaymentDao
) : PaymentRepository {
    
    override suspend fun createPayment(request: CreatePaymentRequest): String? {
        return try {
            val apiResponse = api.createPayment(
                orderNo = request.orderNo,
                amount = request.amount,
                paymentMethod = request.paymentMethod
            )
            parsePaymentNo(apiResponse.string())
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun getPaymentDetail(paymentNo: String): Payment? {
        return dao.getByPaymentNo(paymentNo)
    }
    
    override suspend fun updatePaymentStatus(paymentNo: String, status: Int, transactionNo: String?) {
        dao.update(
            dao.getByPaymentNo(paymentNo)?.copy(
                status = status,
                transactionNo = transactionNo
            )!!
        )
    }
    
    private fun parsePaymentNo(json: String): String? {
        // TODO: 解析返回的支付单号
        return null
    }
}
