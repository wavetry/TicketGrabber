package com.ticket.grabber.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 乘客ViewModel
 */
@HiltViewModel
class PassengerViewModel @Inject constructor() : ViewModel() {
    
    init {
        // 初始化
    }
}

/**
 * 支付ViewModel
 */
@HiltViewModel
class PaymentViewModel @Inject constructor() : ViewModel() {
    
    init {
        // 初始化
    }
}
