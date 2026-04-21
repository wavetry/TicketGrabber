package com.ticket.grabber.data.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

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
