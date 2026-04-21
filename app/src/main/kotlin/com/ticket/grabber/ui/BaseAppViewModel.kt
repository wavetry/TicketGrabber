package com.ticket.grabber.ui

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 应用级别ViewModel基类
 */
abstract class BaseAppViewModel(application: Application) : AndroidViewModel(application) {
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    protected fun setLoading(state: Boolean) {
        _isLoading.value = state
    }
    
    @SuppressLint("CoroutineCreationDuringComposition")
    protected fun <T> runCatchingUi(block: suspend () -> T): Result<T> {
        return kotlinx.coroutines.Dispatchers.IO.run {
            try {
                Result.success(block())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
