package com.ticket.grabber.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ticket.grabber.ui.viewmodel.SearchViewModel
import com.ticket.grabber.ui.viewmodel.SortType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import java.time.LocalDate

/**
 * 添加搜索页到导航图
 */
fun NavGraphBuilder.searchScreen() {
    composable(route = "search") {
        SearchScreen(viewModel = hiltViewModel())
    }
}

/**
 * 搜索页
 */
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("车次查询") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Back navigation */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // 出发站和到达站选择
            StationSelectorRow(viewModel = viewModel)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 日期选择
            DateSelector(viewModel = viewModel)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 查询按钮
            QueryButton(viewModel = viewModel)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 查询结果
            SearchResultSection(viewModel = viewModel)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 搜索历史
            SearchHistorySection(viewModel = viewModel)
        }
    }
}

/**
 * 车站选择行
 */
@Composable
fun StationSelectorRow(
    viewModel: SearchViewModel
) {
    Column {
        // 出发站
        StationSelector(
            label = "出发站",
            selectedStation = viewModel.selectedFromStation,
            keyword = viewModel.fromStationKeyword,
            searchResult = viewModel.fromStationSearchResult,
            onKeywordChange = { viewModel.fromStationKeyword = it },
            onSelect = { viewModel.selectFromStation(it) },
            onClear = { viewModel.clearFromStation() },
            onShowSelector = { viewModel.showFromStationSelector() },
            showSelector = viewModel.showFromStationSearch,
            onShowSearch = { viewModel.showFromStationSelector() },
            onClearSearch = { viewModel.hideStationSearch() }
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 箭头图标
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "到",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 到达站
        StationSelector(
            label = "到达站",
            selectedStation = viewModel.selectedToStation,
            keyword = viewModel.toStationKeyword,
            searchResult = viewModel.toStationSearchResult,
            onKeywordChange = { viewModel.toStationKeyword = it },
            onSelect = { viewModel.selectToStation(it) },
            onClear = { viewModel.clearToStation() },
            onShowSelector = { viewModel.showToStationSelector() },
            showSelector = viewModel.showToStationSearch,
            onShowSearch = { viewModel.showToStationSelector() },
            onClearSearch = { viewModel.hideStationSearch() }
        )
    }
}

/**
 * 车站选择器组件
 */
@Composable
fun StationSelector(
    label: String,
    selectedStation: com.ticket.grabber.data.model.Station?,
    keyword: String,
    searchResult: List<com.ticket.grabber.data.model.Station>,
    onKeywordChange: (String) -> Unit,
    onSelect: (com.ticket.grabber.data.model.Station) -> Unit,
    onClear: () -> Unit,
    onShowSelector: () -> Unit,
    showSelector: Boolean,
    onShowSearch: () -> Unit,
    onClearSearch: () -> Unit
) {
    Column {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        
        if (selectedStation != null) {
            OutlinedTextField(
                value = keyword,
                onValueChange = onKeywordChange,
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = onClear) {
                        Icon(Icons.Default.Clear, contentDescription = "清除")
                    }
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedStation.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = selectedStation.code,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            if (showSelector) {
                StationSearchResult(
                    keyword = keyword,
                    searchResult = searchResult,
                    onSelect = onSelect,
                    onClearSearch = onClearSearch
                )
            }
        } else {
            OutlinedTextField(
                value = keyword,
                onValueChange = { 
                    onKeywordChange(it)
                    onShowSearch()
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "请选择${label}") },
                trailingIcon = {
                    IconButton(onClick = onShowSelector) {
                        Icon(Icons.Default.Search, contentDescription = "搜索")
                    }
                }
            )
            
            if (showSelector) {
                StationSearchResult(
                    keyword = keyword,
                    searchResult = searchResult,
                    onSelect = onSelect,
                    onClearSearch = onClearSearch
                )
            }
        }
    }
}

/**
 * 车站搜索结果列表
 */
@Composable
fun StationSearchResult(
    keyword: String,
    searchResult: List<com.ticket.grabber.data.model.Station>,
    onSelect: (com.ticket.grabber.data.model.Station) -> Unit,
    onClearSearch: () -> Unit
) {
    if (keyword.isNotEmpty() && searchResult.isEmpty()) {
        Text(
            text = "未找到车站",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    } else if (searchResult.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .clickable(onClick = onClearSearch)
        ) {
            searchResult.forEach { station ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable(onClick = {
                            onSelect(station)
                            onClearSearch()
                        }),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = station.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = station.city,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = station.code,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * 日期选择器
 */
@Composable
fun DateSelector(
    viewModel: SearchViewModel
) {
    var showDateSelector by remember { mutableStateOf(false) }
    
    Column {
        Text(text = "出发日期", style = MaterialTheme.typography.bodyMedium)
        
        OutlinedTextField(
            value = viewModel.queryState.lastQuery?.departureDate ?: "",
            onValueChange = { /* Use datepicker for date selection */ },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                IconButton(onClick = {
                    showDateSelector = true
                }) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "选择日期")
                }
            },
            trailingIcon = {
                IconButton(onClick = { /* Clear date */ }) {
                    Icon(Icons.Default.Clear, contentDescription = "清除")
                }
            }
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        DateSelectorButtons()
    }
}

/**
 * 日期选择器按钮
 */
@Composable
fun DateSelectorButtons() {
    val today = LocalDate.now().toString()
    val tomorrow = LocalDate.now().plusDays(1).toString()
    val dayAfterTomorrow = LocalDate.now().plusDays(2).toString()
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = true,
            onClick = { /* TODO */ },
            label = { Text(today) }
        )
        FilterChip(
            selected = false,
            onClick = { /* TODO */ },
            label = { Text(tomorrow) }
        )
        FilterChip(
            selected = false,
            onClick = { /* TODO */ },
            label = { Text(dayAfterTomorrow) }
        )
    }
}

/**
 * 查询按钮
 */
@Composable
fun QueryButton(
    viewModel: SearchViewModel
) {
    Button(
        onClick = {
            val lastQuery = viewModel.queryState.lastQuery
            val departureDate = lastQuery?.departureDate ?: ""
            viewModel.query(
                fromStationCode = viewModel.selectedFromStation?.code ?: "",
                toStationCode = viewModel.selectedToStation?.code ?: "",
                departureDate = departureDate
            )
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = (viewModel.selectedFromStation != null && viewModel.selectedToStation != null),
        contentPadding = PaddingValues(16.dp)
    ) {
        if (viewModel.queryState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "查询中...")
        } else {
            Icon(Icons.Default.Search, contentDescription = "查询")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "查询车次", fontSize = 18.sp)
        }
    }
    
    if (viewModel.queryState.error != null) {
        Text(
            text = viewModel.queryState.error ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

/**
 * 查询结果区域
 */
@Composable
fun SearchResultSection(
    viewModel: SearchViewModel
) {
    when (val result = viewModel.searchResult) {
        is SearchResult.Idle -> {
            EmptyState(
                message = "请填写出发站、到达站和日期后查询",
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        is SearchResult.Success -> {
            TrainList(
                trains = result.trains,
                fromStationCode = result.fromStationCode,
                toStationCode = result.toStationCode,
                departureDate = result.departureDate
            )
        }
    }
}

/**
 * 列车列表
 */
@Composable
fun TrainList(
    trains: List<com.ticket.grabber.data.repository.TrainWithAvailability>,
    fromStationCode: String,
    toStationCode: String,
    departureDate: String
) {
    Column {
        // 筛选和排序
        FilterAndSortBar()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 列表
        LazyColumn {
            items(trains) { train ->
                TrainItem(
                    train = train,
                    fromStationCode = fromStationCode,
                    toStationCode = toStationCode,
                    departureDate = departureDate
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

/**
 * 筛选和排序栏
 */
@Composable
fun FilterAndSortBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "排序方式:", style = MaterialTheme.typography.bodySmall)
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = true,
                onClick = { /* TODO */ },
                label = { Text("时间") }
            )
            FilterChip(
                selected = false,
                onClick = { /* TODO */ },
                label = { Text("时长") }
            )
        }
    }
}

/**
 * 列车项
 */
@Composable
fun TrainItem(
    train: com.ticket.grabber.data.repository.TrainWithAvailability,
    fromStationCode: String,
    toStationCode: String,
    departureDate: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        // 车次信息
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = train.train.trainCode,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${train.train.departureTime} - ${train.train.arrivalTime}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${train.train.duration / 60}小时${train.train.duration % 60}分钟",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 各席别余票信息
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SeatInfoItem(
                seatName = "商务座",
                count = train.businessSeat,
                isSelected = false
            )
            SeatInfoItem(
                seatName = "一等座",
                count = train.firstClassSeat,
                isSelected = false
            )
            SeatInfoItem(
                seatName = "二等座",
                count = train.secondClassSeat,
                isSelected = true
            )
            SeatInfoItem(
                seatName = "硬座",
                count = train.hardSeat,
                isSelected = false
            )
            SeatInfoItem(
                seatName = "软卧",
                count = train.softBed,
                isSelected = false
            )
            SeatInfoItem(
                seatName = "硬卧",
                count = train.hardBed,
                isSelected = false
            )
        }
    }
}

/**
 * 席别信息项
 */
@Composable
fun SeatInfoItem(
    seatName: String,
    count: Int,
    isSelected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = seatName,
            style = MaterialTheme.typography.bodySmall
        )
        
        val color = if (count == 0) {
            MaterialTheme.colorScheme.error
        } else if (count > 0 && count < 10) {
            MaterialTheme.colorScheme.warning
        } else {
            MaterialTheme.colorScheme.success
        }
        
        Text(
            text = if (count == 0) "无" else count.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * 搜索历史区域
 */
@Composable
fun SearchHistorySection(
    viewModel: SearchViewModel
) {
    Column {
        Text(text = "历史记录", style = MaterialTheme.typography.titleMedium)
        
        Spacer(modifier = Modifier.height(8.dp))
        
        if (viewModel.searchHistory.isEmpty()) {
            Text(
                text = "暂无搜索历史",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn {
                items(viewModel.searchHistory) { history ->
                    HistoryItem(
                        history = history,
                        onDelete = { viewModel.deleteSearchHistory(history) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

/**
 * 历史项
 */
@Composable
fun HistoryItem(
    history: com.ticket.grabber.data.model.SearchHistory,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
            .clickable { /* TODO: Reuse history */ }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${history.fromStationName} → ${history.toStationName}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = history.departureDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "删除")
            }
        }
    }
}

/**
 * 空状态
 */
@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.SearchOff,
                contentDescription = "空状态",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 错误状态
 */
@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Error,
                contentDescription = "错误",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onRetry) {
                Text(text = "重试")
            }
        }
    }
}

/**
 * 加载指示器
 */
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
