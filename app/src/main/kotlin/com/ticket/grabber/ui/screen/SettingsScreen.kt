package com.ticket.grabber.ui.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * 添加设置页到导航图
 */
fun NavGraphBuilder.settingsScreen() {
    composable(route = "settings") {
        SettingsScreen()
    }
}

/**
 * 设置页
 */
@Composable
fun SettingsScreen() {
    // TODO: 实现设置页UI
}
