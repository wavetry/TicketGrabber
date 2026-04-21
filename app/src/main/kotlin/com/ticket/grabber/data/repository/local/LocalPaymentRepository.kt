package com.ticket.grabber.data.repository.local

import com.ticket.grabber.data.local.PaymentDao
import com.ticket.grabber.data.model.CreatePaymentRequest
import com.ticket.grabber.data.model.Payment
import com.ticket.grabber.data.repository.PaymentRepository

/**
 * 支付本地仓库实现
 */
class LocalPaymentRepository(
    private val dao: PaymentDao
) : PaymentRepository {
    
    override suspend fun createPayment(request: CreatePaymentRequest): String? {
        val payment = Payment(
            paymentNo = "LOCAL_${System.currentTimeMillis()}",
            orderNo = request.orderNo,
            amount = request.amount,
            paymentMethod = request.paymentMethod,
            status = 0
        )
        dao.insert(payment)
        return payment.paymentNo
    }
    
    override suspend fun getPaymentDetail(paymentNo: String): Payment? {
        return dao.getByPaymentNo(paymentNo)
    }
    
    override suspend fun updatePaymentStatus(paymentNo: String, status: Int, transactionNo: String?) {
        dao.update(dao.getByPaymentNo(paymentNo)?.copy(status = status, transactionNo = transactionNo)!!)
    }
}
