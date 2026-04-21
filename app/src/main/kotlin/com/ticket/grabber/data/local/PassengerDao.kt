package com.ticket.grabber.data.local

import androidx.room.*
import com.ticket.grabber.data.model.Passenger
import kotlinx.coroutines.flow.Flow

/**
 * 乘客数据访问对象
 */
@Dao
interface PassengerDao {
    
    @Query("SELECT * FROM passengers ORDER BY isDefault DESC, createTime ASC")
    suspend fun getAll(): List<Passenger>
    
    @Query("SELECT * FROM passengers WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Passenger?
    
    @Query("SELECT * FROM passengers WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultPassenger(): Passenger?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(passenger: Passenger): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(passengers: List<Passenger>)
    
    @Update
    suspend fun update(passenger: Passenger)
    
    @Delete
    suspend fun delete(passenger: Passenger)
    
    @Query("DELETE FROM passengers WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("DELETE FROM passengers")
    suspend fun clearAll()
    
    @Query("SELECT COUNT(*) FROM passengers")
    suspend fun count(): Int
    
    @Query("UPDATE passengers SET isDefault = 0")
    suspend fun clearAllDefaults()
    
    @Query("UPDATE passengers SET isDefault = 1 WHERE id = :id")
    suspend fun setDefault(id: Long)
}
