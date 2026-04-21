package com.ticket.grabber.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.ticket.grabber.ui.screen.*
import com.ticket.grabber.ui.theme.TicketGrabberTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * 主Activity
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicketGrabberTheme {
                MainScreen()
            }
        }
    }
}

/**
 * 底部导航项
 */
sealed class BottomNavItem(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String
) {
    data object Home : BottomNavItem("home", Icons.Default.Home, "首页")
    data object Search : BottomNavItem("search", Icons.Default.Search, "查询")
    data object Grab : BottomNavItem("grab", Icons.Default.Train, "抢票")
    data object Orders : BottomNavItem("orders", Icons.Default.Receipt, "订单")
    data object Profile : BottomNavItem("profile", Icons.Default.Person, "我的")
}

/**
 * 主屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Grab,
        BottomNavItem.Orders,
        BottomNavItem.Profile
    )
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentDestination?.hierarchy?.any { 
                            it.route == item.route 
                        } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 首页
            composable(BottomNavItem.Home.route) {
                HomeScreen()
            }
            
            // 查询页
            composable(BottomNavItem.Search.route) {
                SearchScreen()
            }
            
            // 抢票页
            composable(BottomNavItem.Grab.route) {
                GrabScreen()
            }
            
            // 订单页
            composable(BottomNavItem.Orders.route) {
                OrderScreen()
            }
            
            // 个人中心
            composable(BottomNavItem.Profile.route) {
                ProfileScreen()
            }
        }
    }
}

/**
 * 个人中心页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("个人中心") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // 用户信息卡片
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "用户名",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "点击登录",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 功能列表
            ListItem(
                headlineContent = { Text("常用乘客") },
                leadingContent = { Icon(Icons.Default.People, null) },
                trailingContent = { Icon(Icons.Default.ChevronRight, null) }
            )
            
            ListItem(
                headlineContent = { Text("抢票设置") },
                leadingContent = { Icon(Icons.Default.Settings, null) },
                trailingContent = { Icon(Icons.Default.ChevronRight, null) }
            )
            
            ListItem(
                headlineContent = { Text("关于我们") },
                leadingContent = { Icon(Icons.Default.Info, null) },
                trailingContent = { Icon(Icons.Default.ChevronRight, null) }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 退出登录按钮
            Button(
                onClick = { /* 退出登录 */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("退出登录")
            }
        }
    }
}
