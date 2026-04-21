package com.ticket.grabber.ui.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ticket.grabber.ui.viewmodel.HomeViewModel

/**
 * 添加首页到导航图
 */
fun NavGraphBuilder.homeScreen() {
    composable(route = Screens.HOME) {
        HomeScreen(viewModel = hiltViewModel())
    }
}

/**
 * 首页
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    // TODO: 实现首页UI
}
