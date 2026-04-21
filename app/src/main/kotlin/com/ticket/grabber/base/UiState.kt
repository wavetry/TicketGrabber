package com.ticket.grabber.base

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String, val data: T? = null) : UiState<T>()
}

sealed class UiEvent<out T> {
    data class ShowMessage(val message: String) : UiEvent<Nothing>()
    data class Navigate(val route: String) : UiEvent<Nothing>()
    data class ShowDialog(val title: String, val message: String) : UiEvent<Nothing>()
    data class ShowBottomSheet(val content: @Composable () -> Unit) : UiEvent<Nothing>()
}
