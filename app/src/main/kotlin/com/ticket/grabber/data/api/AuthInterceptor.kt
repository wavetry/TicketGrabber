package com.ticket.grabber.data.api

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * 认证拦截器
 * 处理用户登录认证
 */
class AuthInterceptor : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // 检查是否需要认证
        if (!originalRequest.url.toString().contains("/auth/")) {
            // TODO: 添加认证token
            // val token = TokenManager.getInstance().getToken()
            // if (token != null) {
            //     val authenticatedRequest = originalRequest.newBuilder()
            //         .addHeader("Authorization", "Bearer $token")
            //         .build()
            //     return chain.proceed(authenticatedRequest)
            // }
        }
        
        return chain.proceed(originalRequest)
    }
}

/**
 * Token拦截器
 * 自动刷新和添加Token
 */
class TokenInterceptor : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("User-Agent", "TicketGrabber/1.0")
            .build()
        
        return chain.proceed(newRequest)
    }
}

/**
 * 日志拦截器
 * 记录请求和响应详情
 */
class LoggingInterceptor(
    private val level: okhttp3.logging.HttpLoggingInterceptor.Level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY,
    private val showRequestBody: Boolean = true,
    private val showResponseBody: Boolean = true
) : Interceptor {
    
    private val loggingInterceptor = okhttp3.logging.HttpLoggingInterceptor().apply {
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
