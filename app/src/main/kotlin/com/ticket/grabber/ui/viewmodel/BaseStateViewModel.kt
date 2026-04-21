package com.ticket.grabber.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * 通用状态ViewModel
 * 提供基本的状态管理
 */
abstract class BaseStateViewModel<State : Any> : ViewModel() {
    
    private val _state = MutableStateFlow(createInitialState())
    val state: StateFlow<State> = _state.asStateFlow()
    
    abstract fun createInitialState(): State
    
    protected fun updateState(reducer: State.() -> State) {
        _state.value = _state.value.reducer()
    }
    
    protected fun setState(newState: State) {
        _state.value = newState
    }
}
