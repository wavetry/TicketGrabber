package com.ticket.grabber.core.network

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 离线缓存拦截器
 * 仅在无网络时使用缓存
 */
class OfflineCacheInterceptor : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        var response: Response = chain.proceed(chain.request())
        
        // 如果是GET请求且成功，设置缓存
        if (chain.request().method == "GET" && response.isSuccessful) {
            val cacheControl = CacheControl.Builder()
                .maxStale(30, java.util.concurrent.TimeUnit.DAYS)
                .build()
            
            response = response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build()
        }
        
        return response
    }
}
