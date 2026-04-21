package com.ticket.grabber.core.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 网络状态拦截器
 * 在请求前检查网络状态
 */
class NetworkStateInterceptor : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // TODO: 在此处添加网络状态检查
        // 如果没有网络，可以返回一个自定义的响应或者抛出异常
        
        return chain.proceed(originalRequest)
    }
}
