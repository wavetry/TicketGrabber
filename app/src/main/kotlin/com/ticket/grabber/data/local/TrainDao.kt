package com.ticket.grabber.data.local

import androidx.room.*
import com.ticket.grabber.data.model.Train
import kotlinx.coroutines.flow.Flow

/**
 * 列车数据访问对象
 */
@Dao
interface TrainDao {
    
    @Query("SELECT * FROM trains ORDER BY departureTime ASC")
    suspend fun getAll(): List<Train>
    
    @Query("SELECT * FROM trains WHERE trainNo = :trainNo LIMIT 1")
    suspend fun getByTrainNo(trainNo: String): Train?
    
    @Query("SELECT * FROM trains WHERE fromStation = :fromStation AND toStation = :toStation ORDER BY departureTime ASC")
    fun getTrainsByRoute(fromStation: String, toStation: String): Flow<List<Train>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trains: List<Train>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(train: Train)
    
    @Delete
    suspend fun delete(train: Train)
    
    @Query("DELETE FROM trains")
    suspend fun clearAll()
    
    @Query("SELECT COUNT(*) FROM trains")
    suspend fun count(): Int
}
