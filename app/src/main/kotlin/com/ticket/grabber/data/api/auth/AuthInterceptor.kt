package com.ticket.grabber.data.api.auth

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 认证拦截器
 * 处理用户登录认证和Cookie管理
 */
class AuthInterceptor(
    private val authRepository: AuthRepository
) : Interceptor {
    
    private val isRefreshing = AtomicBoolean(false)
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()
        
        // 不拦截认证相关请求
        if (url.contains("/auth/") || url.contains("/captcha")) {
            return chain.proceed(originalRequest)
        }
        
        // 对需要认证的请求添加认证信息
        val newRequest = buildAuthenticatedRequest(originalRequest)
        
        return chain.proceed(newRequest)
    }
    
    private fun buildAuthenticatedRequest(originalRequest: Request): Request {
        val builder = originalRequest.newBuilder()
        
        // 添加必要的请求头
        builder.addHeader("Content-Type", "application/x-www-form-urlencoded")
        builder.addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
        builder.addHeader("User-Agent", "TicketGrabber/1.0")
        builder.addHeader("X-Requested-With", "XMLHttpRequest")
        builder.addHeader("Referer", "https://kyfw.12306.cn/")
        builder.addHeader("Origin", "https://kyfw.12306.cn")
        
        return builder.build()
    }
    
    /**
     * 检查响应是否需要重新认证
     */
    fun isAuthRequired(response: Response): Boolean {
        if (response.code == 401 || response.code == 403) {
            return true
        }
        
        // 检查响应体中是否有认证相关的字段
        response.body?.let { body ->
            body.string().let { bodyString ->
                // TODO: 实际检查认证状态的逻辑
                // 对于12306，可能返回特定的JSON表示未登录
            }
        }
        
        return false
    }
}
