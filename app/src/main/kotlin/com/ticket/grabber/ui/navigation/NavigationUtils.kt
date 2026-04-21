package com.ticket.grabber.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

/**
 * 导航辅助函数
 */
object NavigationUtils {
    
    /**
     * 获取NavController
     */
    @Composable
    fun getNavController(): NavHostController {
        return androidx.navigation.compose.rememberNavController()
    }
    
    /**
     * 导航到指定页面
     */
    fun navigate(
        navController: NavHostController,
        route: String,
        arguments: Map<String, Any?> = emptyMap()
    ) {
        val finalRoute = if (arguments.isEmpty()) {
            route
        } else {
            route + arguments.entries.joinToString("?") { "${it.key}={${it.key}}" }
        }
        
        navController.navigate(finalRoute)
    }
    
    /**
     * 返回上一页
     */
    fun navigateUp(navController: NavHostController) {
        navController.navigateUp()
    }
    
    /**
     * 返回到根页面
     */
    fun popToRoot(navController: NavHostController) {
        navController.popBackStack(
            navController.graph.startDestinationId,
            false
        )
    }
}
