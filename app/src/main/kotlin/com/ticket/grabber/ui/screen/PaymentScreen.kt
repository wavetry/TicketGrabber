package com.ticket.grabber.ui.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ticket.grabber.ui.viewmodel.PaymentViewModel

/**
 * 添加支付页到导航图
 */
fun NavGraphBuilder.paymentScreen() {
    composable(route = "payment") {
        PaymentScreen(viewModel = hiltViewModel())
    }
}

/**
 * 支付页
 */
@Composable
fun PaymentScreen(
    viewModel: PaymentViewModel = hiltViewModel()
) {
    // TODO: 实现支付页UI
}
