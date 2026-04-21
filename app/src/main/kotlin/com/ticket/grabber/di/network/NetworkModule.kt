package com.ticket.grabber.di.network

import com.ticket.grabber.data.api.TokenInterceptor
import com.ticket.grabber.data.api.auth.AuthInterceptor
import com.ticket.grabber.data.api.auth.AuthRepository
import com.ticket.grabber.data.api.logging.LoggingInterceptor
import com.ticket.grabber.data.model.DeviceInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * 网络层依赖注入模块
 * 配置OkHttpClient和网络拦截器
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 30L
    private const val WRITE_TIMEOUT = 30L
    
    private const val CACHE_SIZE = 10L * 1024L * 1024L // 10MB
    
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenInterceptor: TokenInterceptor,
        loggingInterceptor: LoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .cache(Cache(createTempCacheFile(), CACHE_SIZE))
            .addInterceptor(authInterceptor)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideAuthInterceptor(authRepository: AuthRepository): AuthInterceptor {
        return AuthInterceptor(authRepository)
    }
    
    @Provides
    @Singleton
    fun provideTokenInterceptor(): TokenInterceptor {
        return TokenInterceptor()
    }
    
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): LoggingInterceptor {
        return LoggingInterceptor(
            level = HttpLoggingInterceptor.Level.BODY,
            showRequestBody = true,
            showResponseBody = true
        )
    }
    
    @Provides
    @Singleton
    fun provideDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            deviceType = "android",
            appVersion = "1.0.0",
            osVersion = "14",
            deviceId = "UNKNOWN"
        )
    }
    
    private fun createTempCacheFile(): File {
        return File.createTempFile("okhttp_cache", null).apply {
            deleteOnExit()
        }
    }
}
