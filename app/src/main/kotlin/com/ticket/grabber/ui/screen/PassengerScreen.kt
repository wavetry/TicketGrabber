package com.ticket.grabber.ui.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ticket.grabber.ui.viewmodel.PassengerViewModel

/**
 * 添加乘客管理页到导航图
 */
fun NavGraphBuilder.passengerScreen() {
    composable(route = "passenger") {
        PassengerScreen(viewModel = hiltViewModel())
    }
}

/**
 * 乘客管理页
 */
@Composable
fun PassengerScreen(
    viewModel: PassengerViewModel = hiltViewModel()
) {
    // TODO: 实现乘客管理页UI
}
