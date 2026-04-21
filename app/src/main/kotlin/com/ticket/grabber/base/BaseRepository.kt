package com.ticket.grabber.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 基础Repository
 * 提供通用的数据访问
 */
abstract class BaseRepository {
    
    protected val _networkError = MutableStateFlow<String?>(null)
    val networkError: StateFlow<String?> = _networkError.asStateFlow()
    
    protected suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return try {
            Result.success(apiCall())
        } catch (e: Exception) {
            _networkError.value = e.message
            Result.failure(e)
        }
    }
    
    protected fun clearNetworkError() {
        _networkError.value = null
    }
}
