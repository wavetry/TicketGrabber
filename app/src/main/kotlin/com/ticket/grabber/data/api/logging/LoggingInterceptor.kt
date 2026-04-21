package com.ticket.grabber.data.api.logging

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

/**
 * 日志拦截器
 * 记录请求和响应详情
 */
class LoggingInterceptor(
    private val level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
    private val showRequestBody: Boolean = true,
    private val showResponseBody: Boolean = true
) : Interceptor {
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = this@LoggingInterceptor.level
    }
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        
        // 记录请求
        if (showRequestBody) {
            val requestBody = request.body
            if (requestBody != null) {
                // TODO: 记录请求体
            }
        }
        
        // 执行请求
        val startTime = System.nanoTime()
        val response = chain.proceed(request)
        val endTime = System.nanoTime()
        
        // 记录响应
        if (showResponseBody) {
            val responseBody = response.body
            if (responseBody != null) {
                // TODO: 记录响应体
            }
        }
        
        return response
    }
}
