package com.ticket.grabber.data.local

import androidx.room.*
import com.ticket.grabber.data.model.Payment
import kotlinx.coroutines.flow.Flow

/**
 * 支付数据访问对象
 */
@Dao
interface PaymentDao {
    
    @Query("SELECT * FROM payments ORDER BY createTime DESC")
    fun getAll(): Flow<List<Payment>>
    
    @Query("SELECT * FROM payments WHERE paymentNo = :paymentNo LIMIT 1")
    suspend fun getByPaymentNo(paymentNo: String): Payment?
    
    @Query("SELECT * FROM payments WHERE orderNo = :orderNo LIMIT 1")
    suspend fun getByOrderNo(orderNo: String): Payment?
    
    @Query("SELECT * FROM payments WHERE status = :status ORDER BY createTime DESC")
    fun getByStatus(status: Int): Flow<List<Payment>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payment: Payment)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(payments: List<Payment>)
    
    @Update
    suspend fun update(payment: Payment)
    
    @Delete
    suspend fun delete(payment: Payment)
    
    @Query("DELETE FROM payments WHERE paymentNo = :paymentNo")
    suspend fun deleteByPaymentNo(paymentNo: String)
    
    @Query("DELETE FROM payments")
    suspend fun clearAll()
    
    @Query("SELECT COUNT(*) FROM payments")
    suspend fun count(): Int
}
