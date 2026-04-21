package com.ticket.grabber.data.local

import androidx.room.*
import com.ticket.grabber.data.model.AuthSession
import kotlinx.coroutines.flow.Flow

/**
 * 认证会话数据访问对象
 */
@Dao
interface AuthDao {
    
    /**
     * 插入或更新认证会话
     */
    @Upsert
    suspend fun insert(session: AuthSession)
    
    /**
     * 插入或更新多个认证会话
     */
    @Upsert
    suspend fun insertAll(sessions: List<AuthSession>)
    
    /**
     * 根据ID获取认证会话
     */
    @Query("SELECT * FROM auth_session WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): AuthSession?
    
    /**
     * 获取所有认证会话
     */
    @Query("SELECT * FROM auth_session")
    fun getAll(): List<AuthSession>
    
    /**
     * 获取所有认证会话（Flow）
     */
    @Query("SELECT * FROM auth_session")
    fun getAllFlow(): Flow<List<AuthSession>>
    
    /**
     * 删除认证会话
     */
    @Delete
    suspend fun delete(session: AuthSession)
    
    /**
     * 删除所有认证会话
     */
    @Query("DELETE FROM auth_session")
    suspend fun deleteAll()
}