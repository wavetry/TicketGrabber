package com.ticket.grabber.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * 后台工作基类
 */
abstract class BaseWorker(
    @ chilledContext context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val WORKER_TAG = "ticket_grabber_worker"
    }
}

/**
 * 列车信息同步工作
 * 定期从服务器获取列车时刻表信息
 */
@HiltWorker
class TrainSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters
) : BaseWorker(appContext, params) {
    
    override suspend fun doWork(): Result {
        // TODO: 实现列车信息同步逻辑
        return Result.success()
    }
}

/**
 * 订单状态检查工作
 * 定期检查用户的订单状态
 */
@HiltWorker
class OrderStatusCheckWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters
) : BaseWorker(appContext, params) {
    
    override suspend fun doWork(): Result {
        // TODO: 实现订单状态检查逻辑
        return Result.success()
    }
}

/**
 * 抢票任务工作
 * 执行抢票的核心逻辑
 */
@HiltWorker
class TicketGrabWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters
) : BaseWorker(appContext, params) {
    
    override suspend fun doWork(): Result {
        // TODO: 实现抢票逻辑
        return Result.success()
    }
}

/**
 * 数据清理工作
 * 清理过期的本地数据
 */
@HiltWorker
class DataCleanupWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters
) : BaseWorker(appContext, params) {
    
    override suspend fun doWork(): Result {
        // TODO: 实现数据清理逻辑
        return Result.success()
    }
}
