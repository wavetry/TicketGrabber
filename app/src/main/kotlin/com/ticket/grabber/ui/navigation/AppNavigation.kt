package com.ticket.grabber.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.ticket.grabber.ui.screen.*

/**
 * 应用导航图
 */
@Composable
fun AppNavHost(
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    startDestination: String = "home",
    navController: NavController = androidx.navigation.compose.rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        loginScreen()
        homeScreen()
        detailScreen()
        searchScreen()
        orderScreen()
        passengerScreen()
        paymentScreen()
        settingsScreen()
    }
}

/**
 * 添加主页到导航图
 */
fun NavGraphBuilder.homeScreen() {
    composable(route = "home") {
        HomeScreen()
    }
}

/**
 * 添加详情页到导航图
 */
fun NavGraphBuilder.detailScreen() {
    composable(
        route = "detail?itemId={itemId}",
        arguments = listOf(
            androidx.navigation.navArgument("itemId") {
                type = androidx.navigation.NavType.StringType
            }
        )
    ) { entry ->
        val itemId = entry.arguments?.getString("itemId") ?: ""
        DetailScreen(
            itemId = itemId,
            viewModel = hiltViewModel<DetailViewModel>()
        )
    }
}

/**
 * 添加搜索页到导航图
 */
fun NavGraphBuilder.searchScreen() {
    composable(
        route = "search?query={query}",
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "ticketgrabber://search?query={query}"
            }
        ),
        arguments = listOf(
            androidx.navigation.navArgument("query") {
                type = androidx.navigation.NavType.StringType
            }
        )
    ) { entry ->
        val query = entry.arguments?.getString("query") ?: ""
        SearchScreen(
            query = query,
            viewModel = hiltViewModel<SearchViewModel>()
        )
    }
}

/**
 * 添加订单页到导航图
 */
fun NavGraphBuilder.orderScreen() {
    composable(route = "order") {
        OrderScreen()
    }
}

/**
 * 添加乘客页到导航图
 */
fun NavGraphBuilder.passengerScreen() {
    composable(route = "passenger") {
        PassengerScreen()
    }
}

/**
 * 添加支付页到导航图
 */
fun NavGraphBuilder.paymentScreen() {
    composable(route = "payment") {
        PaymentScreen()
    }
}

/**
 * 添加设置页到导航图
 */
fun NavGraphBuilder.settingsScreen() {
    composable(route = "settings") {
        SettingsScreen()
    }
}

================
File: F:/git/TicketGrabber/app/src/main/kotlin/com/ticket/grabber/ui/screen/LoginScreen.kt
================
package com.ticket.grabber.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ticket.grabber.R
import com.ticket.grabber.ui.viewmodel.LoginViewModel

/**
 * 添加登录页到导航图
 */
fun NavGraphBuilder.loginScreen() {
    composable(route = "login") {
        LoginScreen(viewModel = hiltViewModel())
    }
}

/**
 * 登录页
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavController = androidx.navigation.compose.rememberNavController()
) {
    val formState by viewModel.formState.collectAsState()
    val loginState by viewModel.loginState.collectAsState()
    val requireCaptcha by viewModel.requireCaptcha.collectAsState()
    val captchaImage by viewModel.captchaImage.collectAsState()
    
    val context = LocalContext.current
    
    // 重置状态
    LaunchedEffect(Unit) {
        viewModel.checkAuthStatus()
    }
    
    // 登录成功导航
    LaunchedEffect(loginState) {
        if (loginState is com.ticket.grabber.base.UiState.Success) {
            // 登录成功，导航到主页
            // navController.navigate("home") {
            //     popUpTo("login") { inclusive = true }
            // }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("12306登录") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "欢迎登录12306",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            // 用户名输入
            OutlinedTextField(
                value = formState.username,
                onValueChange = { viewModel.updateField("username", it) },
                label = { Text("用户名") },
                placeholder = { Text("请输入用户名") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                shape = RoundedCornerShape(8.dp)
            )
            
            // 密码输入
            OutlinedTextField(
                value = formState.password,
                onValueChange = { viewModel.updateField("password", it) },
                label = { Text("密码") },
                placeholder = { Text("请输入密码") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                shape = RoundedCornerShape(8.dp)
            )
            
            // 验证码区域
            if (requireCaptcha) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 验证码图片
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(2f / 1f)
                                .padding(end = 8.dp)
                        ) {
                            if (captchaImage != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(captchaImage),
                                    contentDescription = "验证码图片",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
                                )
                            }
                        }
                        
                        // 刷新验证码按钮
                        TextButton(
                            onClick = { viewModel.refreshCaptcha() },
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text("刷新验证码")
                        }
                    }
                    
                    // 验证码输入框
                    OutlinedTextField(
                        value = formState.captcha,
                        onValueChange = { viewModel.updateField("captcha", it) },
                        label = { Text("验证码") },
                        placeholder = { Text("请输入验证码") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
            
            // 错误提示
            if (loginState is com.ticket.grabber.base.UiState.Error) {
                val errorMessage = when (loginState) {
                    is com.ticket.grabber.base.UiState.Error -> loginState.message
                    else -> ""
                }
                
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 12.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            // 登录按钮
            Button(
                onClick = { viewModel.login() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(top = 16.dp),
                enabled = loginState !is com.ticket.grabber.base.UiState.Loading,
                shape = RoundedCornerShape(8.dp)
            ) {
                if (loginState is com.ticket.grabber.base.UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("登录")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 忘记密码/注册链接
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = { /* TODO: 跳转到忘记密码页面 */ }) {
                    Text("忘记密码")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                TextButton(onClick = { /* TODO: 跳转到注册页面 */ }) {
                    Text("立即注册")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 底部说明
            Text(
                text = "使用12306账号登录本应用",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
