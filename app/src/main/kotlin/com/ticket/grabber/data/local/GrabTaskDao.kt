package com.ticket.grabber.data.local

import androidx.room.*
import com.ticket.grabber.data.model.GrabResult
import com.ticket.grabber.data.model.GrabTask
import kotlinx.coroutines.flow.Flow

/**
 * 抢票任务DAO
 */
@Dao
interface GrabTaskDao {
    
    @Query("SELECT * FROM grab_tasks ORDER BY createTime DESC LIMIT :limit")
    fun getAllTasks(limit: Int = 100): Flow<List<GrabTask>>
    
    @Query("SELECT * FROM grab_tasks WHERE status = :status ORDER BY priority DESC, createTime DESC LIMIT :limit")
    fun getTasksByStatus(status: Int, limit: Int = 100): Flow<List<GrabTask>>
    
    @Query("SELECT * FROM grab_tasks WHERE (:status IS NULL OR status = :status) ORDER BY createTime DESC LIMIT :limit")
    fun getTasks(status: Int?, limit: Int): Flow<List<GrabTask>>
    
    @Query("SELECT * FROM grab_tasks WHERE taskId = :taskId")
    suspend fun getTaskById(taskId: String): GrabTask?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: GrabTask)
    
    @Update
    suspend fun updateTask(task: GrabTask)
    
    @Query("DELETE FROM grab_tasks WHERE taskId = :taskId")
    suspend fun deleteTask(taskId: String)
    
    @Query("DELETE FROM grab_tasks WHERE status IN (2, 3, 4) AND createTime < :timestamp")
    suspend fun deleteOldTasks(timestamp: Long)
    
    @Query("SELECT COUNT(*) FROM grab_tasks WHERE status = 1")
    suspend fun getRunningTaskCount(): Int
}

/**
 * 抢票结果DAO
 */
@Dao
interface GrabResultDao {
    
    @Query("SELECT * FROM grab_results WHERE taskId = :taskId ORDER BY timestamp DESC LIMIT :limit")
    fun getResultsByTask(taskId: String, limit: Int = 50): Flow<List<GrabResult>>
    
    @Query("SELECT * FROM grab_results WHERE taskId = :taskId AND success = 1 ORDER BY timestamp DESC LIMIT 1")
    suspend fun getSuccessResult(taskId: String): GrabResult?
    
    @Query("SELECT * FROM grab_results WHERE (:taskId IS NULL OR taskId = :taskId) AND (:success IS NULL OR success = :success) ORDER BY timestamp DESC LIMIT :limit")
    fun getResults(taskId: String?, success: Boolean?, limit: Int): Flow<List<GrabResult>>
    
    @Query("SELECT * FROM grab_results WHERE id = :id")
    suspend fun getById(id: Long): GrabResult?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: GrabResult)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(results: List<GrabResult>)
    
    @Update
    suspend fun update(result: GrabResult)
    
    @Delete
    suspend fun delete(result: GrabResult)
    
    @Query("DELETE FROM grab_results WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("DELETE FROM grab_results WHERE taskId = :taskId")
    suspend fun deleteByTaskId(taskId: String)
    
    @Query("DELETE FROM grab_results")
    suspend fun clearAll()
    
    @Query("SELECT COUNT(*) FROM grab_results WHERE taskId = :taskId")
    suspend fun getResultCount(taskId: String): Int
}