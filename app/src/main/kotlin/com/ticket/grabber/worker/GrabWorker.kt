package com.ticket.grabber.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.ticket.grabber.data.model.GrabTaskStatus
import com.ticket.grabber.data.repository.GrabTaskRepository
import com.ticket.grabber.data.repository.TicketRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

/**
 * 抢票Worker
 * 后台执行抢票任务
 */
@HiltWorker
class GrabWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val grabTaskRepository: GrabTaskRepository,
    private val ticketRepository: TicketRepository
) : CoroutineWorker(context, params) {
    
    companion object {
        const val WORK_NAME = "grab_worker"
        const val KEY_TASK_ID = "task_id"
        const val NOTIFICATION_CHANNEL_ID = "grab_channel"
        const val NOTIFICATION_ID = 1
        
        /**
         * 创建并启动抢票Worker
         */
        fun start(context: Context, taskId: String) {
            val workRequest = OneTimeWorkRequestBuilder<GrabWorker>()
                .setInputData(workDataOf(KEY_TASK_ID to taskId))
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()
            
            WorkManager.getInstance(context).enqueueUniqueWork(
                "$WORK_NAME-$taskId",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
        
        /**
         * 取消抢票Worker
         */
        fun cancel(context: Context, taskId: String) {
            WorkManager.getInstance(context).cancelUniqueWork("$WORK_NAME-$taskId")
        }
    }
    
    override suspend fun doWork(): Result {
        val taskId = inputData.getString(KEY_TASK_ID) 
            ?: return Result.failure()
        
        createNotificationChannel()
        
        return try {
            val taskResult = grabTaskRepository.getTask(taskId)
            if (taskResult.isFailure) {
                return Result.failure()
            }
            
            val task = taskResult.getOrNull() 
                ?: return Result.failure()
            
            if (task.status != GrabTaskStatus.RUNNING.code) {
                return Result.success()
            }
            
            // 抢票循环
            while (task.status == GrabTaskStatus.RUNNING.code) {
                // 检查是否超过最大重试次数
                if (task.retryCount >= task.maxRetryCount) {
                    grabTaskRepository.updateTaskStatus(
                        taskId,
                        GrabTaskStatus.FAILED,
                        errorMessage = "超过最大重试次数"
                    )
                    showNotification("抢票失败", "任务${task.trainCode}超过最大重试次数")
                    return Result.failure()
                }
                
                // 查询余票
                val availabilityResult = ticketRepository.checkTicketAvailability(
                    task.trainNo,
                    task.fromStation,
                    task.toStation,
                    task.departureDate
                )
                
                if (availabilityResult.isSuccess) {
                    val availability = availabilityResult.getOrNull()
                    if (availability != null && availability.hasTickets) {
                        // 有余票，尝试提交订单
                        // 这里简化处理，实际需要调用订单提交API
                        grabTaskRepository.updateTaskStatus(
                            taskId,
                            GrabTaskStatus.SUCCESS,
                            orderNo = "ORDER_${System.currentTimeMillis()}"
                        )
                        showNotification(
                            "抢票成功！",
                            "成功抢到 ${task.trainCode} 车票"
                        )
                        return Result.success()
                    }
                }
                
                // 增加重试次数
                grabTaskRepository.incrementRetryCount(taskId)
                
                // 更新进度通知
                setForegroundAsync(
                    ForegroundInfo(
                        NOTIFICATION_ID,
                        createProgressNotification(task)
                    )
                )
                
                // 等待下次查询
                delay(task.queryInterval)
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "抢票服务",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "后台抢票任务通知"
            }
            
            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createProgressNotification(task: com.ticket.grabber.data.model.GrabTask): android.app.Notification {
        return NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("正在抢票: ${task.trainCode}")
            .setContentText("已尝试 ${task.retryCount}/${task.maxRetryCount} 次")
            .setSmallIcon(android.R.drawable.ic_menu_recent_history)
            .setOngoing(true)
            .setProgress(task.maxRetryCount, task.retryCount, false)
            .build()
    }
    
    private fun showNotification(title: String, message: String) {
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .build()
        
        val notificationManager = applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
