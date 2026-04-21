package com.ticket.grabber.di.worker

import androidx.work.WorkerApplicationConfiguration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * WorkManager依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {
    
    @Provides
    @Singleton
    fun provideWorkerConfiguration(): WorkerApplicationConfiguration {
        return WorkerApplicationConfiguration.Builder().build()
    }
}
