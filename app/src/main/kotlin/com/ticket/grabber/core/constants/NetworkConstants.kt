package com.ticket.grabber.core.constants

/**
 * 网络常量定义
 */
object NetworkConstants {
    
    // HTTP状态码
    const val HTTP_OK = 200
    const val HTTP_CREATED = 201
    const val HTTP_BAD_REQUEST = 400
    const val HTTP_UNAUTHORIZED = 401
    const val HTTP_FORBIDDEN = 403
    const val HTTP_NOT_FOUND = 404
    const val HTTP_INTERNAL_ERROR = 500
    
    // 请求头
    const val HEADER_CONTENT_TYPE = "Content-Type"
    const val HEADER_ACCEPT = "Accept"
    const val HEADER_AUTHORIZATION = "Authorization"
    const val HEADER_DEVICE_ID = "Device-Id"
    const val HEADER_APP_VERSION = "App-Version"
    
    // 内容类型
    const val CONTENT_TYPE_JSON = "application/json"
    const val CONTENT_TYPE_FORM = "application/x-www-form-urlencoded"
    
    // 错误消息
    const val ERROR_NO_INTERNET = "网络连接不可用，请检查您的网络设置"
    const val ERROR_TIMEOUT = "请求超时，请重试"
    const val ERROR_SERVER = "服务器错误，请稍后重试"
    const val ERROR_AUTH = "认证失败，请重新登录"
    const val ERROR_UNKNOWN = "未知错误，请重试"
}
