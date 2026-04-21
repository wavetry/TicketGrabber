package com.ticket.grabber.data.local

import androidx.room.*
import com.ticket.grabber.data.model.Order
import kotlinx.coroutines.flow.Flow

/**
 * 订单数据访问对象
 */
@Dao
interface OrderDao {
    
    @Query("SELECT * FROM orders ORDER BY createTime DESC")
    fun getAll(): Flow<List<Order>>
    
    @Query("SELECT * FROM orders WHERE orderNo = :orderNo LIMIT 1")
    suspend fun getByOrderNo(orderNo: String): Order?
    
    @Query("SELECT * FROM orders WHERE status = :status ORDER BY createTime DESC")
    fun getByStatus(status: Int): Flow<List<Order>>
    
    @Query("SELECT * FROM orders WHERE trainNo = :trainNo ORDER BY departureDate ASC")
    fun getByTrainNo(trainNo: String): Flow<List<Order>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: Order)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<Order>)
    
    @Update
    suspend fun update(order: Order)
    
    @Delete
    suspend fun delete(order: Order)
    
    @Query("DELETE FROM orders WHERE orderNo = :orderNo")
    suspend fun deleteByOrderNo(orderNo: String)
    
    @Query("DELETE FROM orders")
    suspend fun clearAll()
    
    @Query("SELECT COUNT(*) FROM orders")
    suspend fun count(): Int
}
