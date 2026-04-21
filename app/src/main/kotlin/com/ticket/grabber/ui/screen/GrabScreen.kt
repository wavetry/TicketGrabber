package com.ticket.grabber.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ticket.grabber.data.model.GrabResult
import com.ticket.grabber.data.model.GrabTask
import com.ticket.grabber.data.model.GrabTaskStatus
import com.ticket.grabber.ui.viewmodel.GrabViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * 添加抢票页到导航图
 */
fun NavGraphBuilder.grabScreen() {
    composable(route = "grab") {
        GrabScreen(viewModel = hiltViewModel())
    }
}

/**
 * 抢票页
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrabScreen(
    viewModel: GrabViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var showTaskDetail by remember { mutableStateOf<GrabTask?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("抢票任务") },
                actions = {
                    IconButton(onClick = { viewModel.loadTasks() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "刷新")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "新建抢票")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading && uiState.tasks.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.tasks.isEmpty() -> {
                    EmptyGrabTaskView(
                        onCreateClick = { showCreateDialog = true }
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.tasks) { task ->
                            GrabTaskCard(
                                task = task,
                                onStart = { viewModel.startTask(task.taskId) },
                                onStop = { viewModel.stopTask(task.taskId) },
                                onCancel = { viewModel.cancelTask(task.taskId) },
                                onDelete = { viewModel.deleteTask(task.taskId) },
                                onDetail = { showTaskDetail = task }
                            )
                        }
                    }
                }
            }
            
            // 错误提示
            uiState.error?.let { error ->
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    action = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("确定")
                        }
                    }
                ) {
                    Text(error)
                }
            }
        }
    }
    
    // 创建任务对话框
    if (showCreateDialog) {
        CreateTaskDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = {
                viewModel.createTask()
                showCreateDialog = false
            }
        )
    }
    
    // 任务详情对话框
    showTaskDetail?.let { task ->
        TaskDetailDialog(
            task = task,
            onDismiss = { showTaskDetail = null }
        )
    }
}

/**
 * 空任务视图
 */
@Composable
private fun EmptyGrabTaskView(
    onCreateClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Train,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "暂无抢票任务",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "点击右下角按钮创建抢票任务",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onCreateClick) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("创建抢票任务")
        }
    }
}

/**
 * 抢票任务卡片
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GrabTaskCard(
    task: GrabTask,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
    onDetail: () -> Unit
) {
    val status = GrabTaskStatus.values().find { it.code == task.status } ?: GrabTaskStatus.PENDING
    val statusColor = when (status) {
        GrabTaskStatus.PENDING -> MaterialTheme.colorScheme.outline
        GrabTaskStatus.RUNNING -> MaterialTheme.colorScheme.primary
        GrabTaskStatus.SUCCESS -> MaterialTheme.colorScheme.tertiary
        GrabTaskStatus.FAILED -> MaterialTheme.colorScheme.error
        GrabTaskStatus.CANCELLED -> MaterialTheme.colorScheme.outline
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 头部：车次信息和状态
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.trainCode,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                AssistChip(
                    onClick = onDetail,
                    label = { Text(status.name) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = statusColor.copy(alpha = 0.1f),
                        labelColor = statusColor
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 行程信息
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.fromStation,
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = task.toStation,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "出发日期: ${task.departureDate}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            
            // 席别信息
            Text(
                text = "席别: ${task.seatTypes}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            // 进度信息
            if (task.status == GrabTaskStatus.RUNNING.code) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { task.retryCount.toFloat() / task.maxRetryCount },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "已尝试 ${task.retryCount}/${task.maxRetryCount} 次",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            
            // 操作按钮
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                when (status) {
                    GrabTaskStatus.PENDING, GrabTaskStatus.FAILED -> {
                        TextButton(onClick = onStart) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("开始")
                        }
                    }
                    GrabTaskStatus.RUNNING -> {
                        TextButton(onClick = onStop) {
                            Icon(Icons.Default.Pause, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("暂停")
                        }
                        TextButton(onClick = onCancel) {
                            Icon(Icons.Default.Close, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("取消")
                        }
                    }
                    else -> {}
                }
                TextButton(
                    onClick = onDetail,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Info, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("详情")
                }
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("删除")
                }
            }
        }
    }
}

/**
 * 任务详情对话框
 */
@Composable
private fun TaskDetailDialog(
    task: GrabTask,
    onDismiss: () -> Unit
) {
    val status = GrabTaskStatus.values().find { it.code == task.status } ?: GrabTaskStatus.PENDING
    val statusColor = when (status) {
        GrabTaskStatus.PENDING -> MaterialTheme.colorScheme.outline
        GrabTaskStatus.RUNNING -> MaterialTheme.colorScheme.primary
        GrabTaskStatus.SUCCESS -> MaterialTheme.colorScheme.tertiary
        GrabTaskStatus.FAILED -> MaterialTheme.colorScheme.error
        GrabTaskStatus.CANCELLED -> MaterialTheme.colorScheme.outline
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(task.trainCode)
                Text(
                    text = status.name,
                    color = statusColor
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("车次: ${task.trainCode} (${task.trainNo})")
                Text("路线: ${task.fromStation} → ${task.toStation}")
                Text("日期: ${task.departureDate}")
                Text("席别: ${task.seatTypes}")
                Text("重试: ${task.retryCount}/${task.maxRetryCount}")
                if (task.orderNo != null) {
                    Text("订单号: ${task.orderNo}")
                }
                if (task.errorMessage != null) {
                    Text("错误: ${task.errorMessage}")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭")
            }
        }
    )
}

/**
 * 创建任务对话框
 */
@Composable
private fun CreateTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("创建抢票任务") },
        text = {
            Text("请先查询车次，然后选择车次创建抢票任务")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}