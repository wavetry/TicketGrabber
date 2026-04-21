package com.ticket.grabber.data.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 设备信息拦截器
 * 添加设备信息到请求头
 */
class DeviceInfoInterceptor : Interceptor {
    
    private var deviceInfo: DeviceInfo? = null
    
    fun setDeviceInfo(deviceInfo: DeviceInfo) {
        this.deviceInfo = deviceInfo
    }
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        val newRequest = deviceInfo?.let { info ->
            originalRequest.newBuilder()
                .addHeader("Device-Type", info.deviceType)
                .addHeader("App-Version", info.appVersion)
                .addHeader("OS-Version", info.osVersion)
                .addHeader("Device-Id", info.deviceId)
                .build()
        } ?: originalRequest
        
        return chain.proceed(newRequest)
    }
}

/**
 * 设备信息数据类
 */
data class DeviceInfo(
    val deviceType: String,
    val appVersion: String,
    val osVersion: String,
    val deviceId: String
)
