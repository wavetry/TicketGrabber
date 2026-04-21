package com.ticket.grabber.data.local

import androidx.room.*
import com.ticket.grabber.data.model.SearchHistory
import kotlinx.coroutines.flow.Flow

/**
 * 搜索历史数据访问对象
 */
@Dao
interface SearchHistoryDao {
    
    @Query("SELECT * FROM search_history ORDER BY queryTime DESC LIMIT :limit OFFSET :offset")
    fun getHistory(query: SearchHistoryQuery): Flow<List<SearchHistory>>
    
    @Query("SELECT * FROM search_history ORDER BY queryTime DESC LIMIT :limit")
    suspend fun getAllRecent(limit: Int = 20): List<SearchHistory>
    
    @Query("SELECT * FROM search_history WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): SearchHistory?
    
    @Query("SELECT COUNT(*) FROM search_history")
    suspend fun count(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: SearchHistory)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(histories: List<SearchHistory>)
    
    @Update
    suspend fun update(history: SearchHistory)
    
    @Delete
    suspend fun delete(history: SearchHistory)
    
    @Query("DELETE FROM search_history WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("DELETE FROM search_history")
    suspend fun clearAll()
    
    @Query("DELETE FROM search_history WHERE id NOT IN (SELECT id FROM search_history ORDER BY queryTime DESC LIMIT :keep)")
    suspend fun clearOldHistory(keep: Int = 50)
}
