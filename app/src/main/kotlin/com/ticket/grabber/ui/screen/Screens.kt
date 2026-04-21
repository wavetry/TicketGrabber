package com.ticket.grabber.ui.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * 屏幕路由定义
 */
object Screens {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val HOME = "home"
    const val SEARCH = "search"
    const val ORDER = "order"
    const val PASSENGER = "passenger"
    const val SETTINGS = "settings"
}

/**
 * 导航路由扩展
 */
fun NavGraphBuilder.screen(
    route: String,
    openScreen: @Composable () -> Unit
) {
    composable(route) {
        openScreen()
    }
}

/**
 * 页面导航辅助函数
 */
fun NavController.navigateScreen(route: String) {
    navigate(route) {
        popUpTo(0) { inclusive = true }
    }
}
