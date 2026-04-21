package com.ticket.grabber.ui.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

/**
 * 订单详情屏幕参数
 */
const val ORDER_DETAIL_ROUTE = "order_detail?orderNo={orderNo}"
const val ORDER_DETAIL_ORDER_NO_ARG = "orderNo"

/**
 * 添加订单详情屏幕到导航图
 */
fun NavGraphBuilder.orderDetailScreen(
    onNavigateBack: () -> Unit
) {
    composable(
        route = ORDER_DETAIL_ROUTE,
        arguments = listOf(
            navArgument(ORDER_DETAIL_ORDER_NO_ARG) {
                type = NavType.StringType
            }
        )
    ) { entry ->
        val orderNo = entry.arguments?.getString(ORDER_DETAIL_ORDER_NO_ARG) ?: ""
        OrderDetailScreen(
            orderNo = orderNo,
            onNavigateBack = onNavigateBack
        )
    }
}

/**
 * 订单详情屏幕
 */
@Composable
fun OrderDetailScreen(
    orderNo: String,
    onNavigateBack: () -> Unit,
    viewModel: OrderDetailViewModel = hiltViewModel()
) {
    // TODO: 实现订单详情UI
}
