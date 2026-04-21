package com.ticket.grabber.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 基础ViewModel
 * 提供通用的状态管理
 */
abstract class BaseViewModel<State : Any, Event : Any> : ViewModel() {
    
    private val _uiState = MutableStateFlow(State::class.java.getDeclaredConstructor().newInstance())
    val uiState: StateFlow<State> = _uiState.asStateFlow()
    
    private val _uiEvent = MutableStateFlow(Event::class.java.getDeclaredConstructor().newInstance())
    val uiEvent: StateFlow<Event> = _uiEvent.asStateFlow()
    
    protected fun updateState(update: State.() -> State) {
        _uiState.value = _uiState.value.update()
    }
    
    protected fun emitEvent(event: Event) {
        _uiEvent.value = event
    }
    
    protected fun emitError(message: String) {
        emitEvent(Event::class.java.getDeclaredConstructor().newInstance().apply {
            // TODO: 设置错误信息
        })
    }
}

/**
 * 空状态
 */
data object EmptyState

/**
 * 空事件
 */
data object EmptyEvent
