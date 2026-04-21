package com.ticket.grabber.base

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * 基础SavedStateViewModel
 * 支持状态恢复
 */
abstract class BaseSavedStateViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    protected val savedStateHandle = savedStateHandle
}
