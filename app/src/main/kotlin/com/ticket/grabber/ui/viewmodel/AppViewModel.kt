package com.ticket.grabber.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 应用ViewModel
 */
@HiltViewModel
class AppViewModel @Inject constructor() : ViewModel() {
    
    init {
        // 初始化应用数据
    }
}

/**
 * 设置ViewModel
 */
@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    
    init {
        // 初始化设置数据
    }
}
