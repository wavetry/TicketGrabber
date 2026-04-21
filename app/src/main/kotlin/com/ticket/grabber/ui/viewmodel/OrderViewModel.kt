package com.ticket.grabber.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ticket.grabber.data.model.Order
import com.ticket.grabber.data.model.OrderQuery
import com.ticket.grabber.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 订单UI状态
 */
data class OrderUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedOrder: Order? = null
)

/**
 * 订单ViewModel
 */
@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()
    
    init {
        loadOrders()
    }
    
    /**
     * 加载订单列表
     */
    fun loadOrders(query: OrderQuery = OrderQuery()) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            orderRepository.getOrders(query)
                .catch { e ->
                    _uiState.update { 
                        it.copy(isLoading = false, error = e.message) 
                    }
                }
                .collect { orders ->
                    _uiState.update { 
                        it.copy(orders = orders, isLoading = false) 
                    }
                }
        }
    }
    
    /**
     * 取消订单
     */
    fun cancelOrder(orderNo: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            orderRepository.cancelOrder(orderNo)
                .onSuccess {
                    loadOrders() // 刷新列表
                }
                .onFailure { e ->
                    _uiState.update { 
                        it.copy(isLoading = false, error = e.message) 
                    }
                }
        }
    }
    
    /**
     * 选择订单
     */
    fun selectOrder(order: Order) {
        _uiState.update { it.copy(selectedOrder = order) }
    }
    
    /**
     * 清除错误
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
