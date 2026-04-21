package com.ticket.grabber.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

/**
 * 基础CoroutineViewModel
 * 提供协程作用域和生命周期管理
 */
abstract class BaseCoroutineViewModel : ViewModel() {
    
    protected val viewModelScope = viewModelScope
    
    protected fun <T> launchInBackground(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> T
    ): Job {
        return viewModelScope.launch(start = start, block = block)
    }
    
    protected fun launchAndCopy(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModelScope.launch(start = start, block = block)
    }
}
