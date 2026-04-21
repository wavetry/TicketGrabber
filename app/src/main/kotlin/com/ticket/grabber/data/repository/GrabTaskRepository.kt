package com.ticket.grabber.data.repository

import com.ticket.grabber.data.api.TicketApi
import com.ticket.grabber.data.local.GrabResultDao
import com.ticket.grabber.data.local.GrabTaskDao
import com.ticket.grabber.data.model.*
import kotlinx.coroutines.flow.Flow

/**
 * 抢票任务仓库接口
 */
interface GrabTaskRepository {
    /**
     * 创建抢票任务
     */
    suspend fun createTask(request: CreateGrabTaskRequest): Result<GrabTask>
    
    /**
     * 启动抢票任务
     */
    suspend fun startTask(taskId: String): Result<GrabTask>
    
    /**
     * 停止抢票任务
     */
    suspend fun stopTask(taskId: String): Result<GrabTask>
    
    /**
     * 取消抢票任务
     */
    suspend fun cancelTask(taskId: String): Result<GrabTask>
    
    /**
     * 删除抢票任务
     */
    suspend fun deleteTask(taskId: String): Result<Boolean>
    
    /**
     * 获取抢票任务列表
     */
    fun getTasks(query: GrabTaskQuery = GrabTaskQuery()): Flow<List<GrabTask>>
    
    /**
     * 获取抢票任务详情
     */
    suspend fun getTask(taskId: String): Result<GrabTask>
    
    /**
     * 获取运行中的任务
     */
    fun getRunningTasks(): Flow<List<GrabTask>>
    
    /**
     * 更新任务状态
     */
    suspend fun updateTaskStatus(
        taskId: String,
        status: GrabTaskStatus,
        orderNo: String? = null,
        errorMessage: String? = null
    ): Result<GrabTask>
    
    /**
     * 增加重试次数
     */
    suspend fun incrementRetryCount(taskId: String): Result<GrabTask>
}

/**
 * 抢票任务仓库实现
 */
class GrabTaskRepositoryImpl(
    private val ticketApi: TicketApi,
    private val taskDao: GrabTaskDao,
    private val resultDao: GrabResultDao,
    private val ticketRepository: TicketRepository
) : GrabTaskRepository {
    
    override suspend fun createTask(request: CreateGrabTaskRequest): Result<GrabTask> {
        return try {
            val task = GrabTask(
                taskId = System.currentTimeMillis().toString(),
                trainNo = request.trainNo,
                trainCode = request.trainCode,
                fromStation = request.fromStation,
                toStation = request.toStation,
                departureDate = request.departureDate,
                seatTypes = request.seatTypes.joinToString(","),
                passengers = request.passengerIds.joinToString(","),
                priority = request.priority,
                maxRetryCount = request.maxRetryCount,
                queryInterval = request.queryInterval
            )
            taskDao.insertTask(task)
            Result.success(task)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun startTask(taskId: String): Result<GrabTask> {
        return try {
            val task = taskDao.getTaskById(taskId) 
                ?: return Result.failure(Exception("Task not found"))
            
            if (task.status != GrabTaskStatus.PENDING.code && 
                task.status != GrabTaskStatus.FAILED.code) {
                return Result.failure(Exception("Task cannot be started"))
            }
            
            val updatedTask = task.copy(
                status = GrabTaskStatus.RUNNING.code,
                startTime = System.currentTimeMillis(),
                retryCount = 0,
                updateTime = System.currentTimeMillis()
            )
            taskDao.updateTask(updatedTask)
            Result.success(updatedTask)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun stopTask(taskId: String): Result<GrabTask> {
        return try {
            val task = taskDao.getTaskById(taskId)
                ?: return Result.failure(Exception("Task not found"))
            
            val updatedTask = task.copy(
                status = GrabTaskStatus.PENDING.code,
                updateTime = System.currentTimeMillis()
            )
            taskDao.updateTask(updatedTask)
            Result.success(updatedTask)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun cancelTask(taskId: String): Result<GrabTask> {
        return try {
            val task = taskDao.getTaskById(taskId)
                ?: return Result.failure(Exception("Task not found"))
            
            val updatedTask = task.copy(
                status = GrabTaskStatus.CANCELLED.code,
                endTime = System.currentTimeMillis(),
                updateTime = System.currentTimeMillis()
            )
            taskDao.updateTask(updatedTask)
            Result.success(updatedTask)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteTask(taskId: String): Result<Boolean> {
        return try {
            taskDao.deleteTask(taskId)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getTasks(query: GrabTaskQuery): Flow<List<GrabTask>> {
        return taskDao.getTasks(query.status, query.size)
    }
    
    override suspend fun getTask(taskId: String): Result<GrabTask> {
        return try {
            val task = taskDao.getTaskById(taskId)
            if (task != null) {
                Result.success(task)
            } else {
                Result.failure(Exception("Task not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getRunningTasks(): Flow<List<GrabTask>> {
        return taskDao.getTasksByStatus(GrabTaskStatus.RUNNING.code)
    }
    
    override suspend fun updateTaskStatus(
        taskId: String,
        status: GrabTaskStatus,
        orderNo: String?,
        errorMessage: String?
    ): Result<GrabTask> {
        return try {
            val task = taskDao.getTaskById(taskId)
                ?: return Result.failure(Exception("Task not found"))
            
            val updatedTask = task.copy(
                status = status.code,
                orderNo = orderNo ?: task.orderNo,
                errorMessage = errorMessage ?: task.errorMessage,
                endTime = if (status == GrabTaskStatus.SUCCESS || 
                             status == GrabTaskStatus.FAILED || 
                             status == GrabTaskStatus.CANCELLED) {
                    System.currentTimeMillis()
                } else task.endTime,
                updateTime = System.currentTimeMillis()
            )
            taskDao.updateTask(updatedTask)
            Result.success(updatedTask)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun incrementRetryCount(taskId: String): Result<GrabTask> {
        return try {
            val task = taskDao.getTaskById(taskId)
                ?: return Result.failure(Exception("Task not found"))
            
            val updatedTask = task.copy(
                retryCount = task.retryCount + 1,
                updateTime = System.currentTimeMillis()
            )
            taskDao.updateTask(updatedTask)
            Result.success(updatedTask)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
