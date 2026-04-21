package com.ticket.grabber.core.constants

/**
 * 应用常量定义
 */
object AppConstants {
    
    // 应用信息
    const val APP_NAME = "TicketGrabber"
    const val APP_VERSION = "1.0.0"
    
    // API相关
    const val BASE_URL = "https://api.12306.cn/"
    const val API_TIMEOUT = 30L
    const val CACHE_TIMEOUT = 3600L // 1小时
    
    // 数据库相关
    const val DATABASE_NAME = "ticket_grabber.db"
    const val DATABASE_VERSION = 1
    
    // 分页相关
    const val DEFAULT_PAGE_SIZE = 20
    const val PAGE_SIZE_CHANGE_DEBOUNCE = 500L
    
    // 工作标签
    const val WORKER_TAG_SYNC = "sync_worker"
    const val WORKER_TAG_GRAB = "grab_worker"
    const val WORKER_TAG_CHECK = "check_worker"
    const val WORKER_TAG_CLEANUP = "cleanup_worker"
}
