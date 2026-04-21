package com.ticket.grabber

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import com.ticket.grabber.data.local.StationDataInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TicketGrabberApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // 初始化车站数据（在后台线程）
        Thread {
            try {
                // We can't access DAO here directly without database instance
                // This would be handled by a ContentProvider or WorkManager
            } catch (e: Exception) {
                // Ignore initialization errors
            }
        }.start()
    }
}
