package com.ticket.grabber.core.network

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 缓存拦截器
 * 处理HTTP缓存策略
 */
class CacheInterceptor : Interceptor {
    
    private var maxStale = 60 * 60 * 24 // 24小时
    
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        
        // 如果没有网络，使用缓存
        // if (!NetworkUtils.isNetworkAvailable(context)) {
        //     request = request.newBuilder()
        //         .cacheControl(CacheControl.FORCE_CACHE)
        //         .build()
        // }
        
        val response = chain.proceed(request)
        
        // 设置响应缓存
        val cacheControl = CacheControl.Builder()
            .maxAge(0, java.util.concurrent.TimeUnit.SECONDS)
            .build()
        
        return response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .removeHeader("Pragma") // 清除旧的缓存头
            .build()
    }
}
