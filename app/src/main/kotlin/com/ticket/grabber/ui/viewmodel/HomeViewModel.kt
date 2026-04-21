package com.ticket.grabber.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 主页ViewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    
    init {
        // 初始化数据
        loadHomeData()
    }
    
    private fun loadHomeData() {
        viewModelScope.launch {
            // TODO: 加载主页数据
        }
    }
}

/**
 * 搜索ViewModel
 */
@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {
    
    init {
        // 初始化
    }
}

/**
 * 登录ViewModel
 */
@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    
    init {
        // 初始化
    }
}

/**
 * 订单详情ViewModel
 */
@HiltViewModel
class OrderDetailViewModel @Inject constructor() : ViewModel() {
    
    init {
        // 初始化
    }
}
