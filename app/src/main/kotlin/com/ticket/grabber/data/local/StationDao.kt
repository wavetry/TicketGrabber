package com.ticket.grabber.data.local

import androidx.room.*
import com.ticket.grabber.data.model.Station
import kotlinx.coroutines.flow.Flow

/**
 * 车站数据访问对象
 */
@Dao
interface StationDao {
    
    @Query("SELECT * FROM stations ORDER BY name ASC")
    suspend fun getAll(): List<Station>
    
    @Query("SELECT * FROM stations WHERE code = :code LIMIT 1")
    suspend fun getByCode(code: String): Station?
    
    @Query("SELECT * FROM stations WHERE name LIKE :keyword OR pinyin LIKE :keyword LIMIT :limit")
    fun search(keyword: String, limit: Int = 20): Flow<List<Station>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stations: List<Station>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(station: Station)
    
    @Delete
    suspend fun delete(station: Station)
    
    @Query("DELETE FROM stations")
    suspend fun clearAll()
    
    @Query("SELECT COUNT(*) FROM stations")
    suspend fun count(): Int
}
