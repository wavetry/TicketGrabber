package com.ticket.grabber.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ticket.grabber.data.model.SearchHistory
import com.ticket.grabber.data.model.Station
import com.ticket.grabber.data.model.TrainQuery
import com.ticket.grabber.data.repository.StationRepository
import com.ticket.grabber.data.repository.TrainRepository
import com.ticket.grabber.data.repository.TrainWithAvailability
import com.ticket.grabber.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 搜索页面ViewModel
 * 负责管理车次查询相关的状态和逻辑
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val stationRepository: StationRepository,
    private val trainRepository: TrainRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {
    
    // 查询状态
    var queryState by mutableStateOf(QueryState())
        private set
    
    // 搜索历史列表
    var searchHistory by mutableStateOf<List<SearchHistory>>(emptyList())
        private set
    
    // 搜索结果
    var searchResult by mutableStateOf<SearchResult>(SearchResult.Idle)
        private set
    
    // 当前选择的车站
    var selectedFromStation by mutableStateOf<Station?>(null)
        private set
    var selectedToStation by mutableStateOf<Station?>(null)
        private set
    
    // 车站搜索关键词
    var fromStationKeyword by mutableStateOf("")
        private set
    var toStationKeyword by mutableStateOf("")
        private set
    
    // 车站搜索结果
    var fromStationSearchResult by mutableStateOf<List<Station>>(emptyList())
        private set
    var toStationSearchResult by mutableStateOf<List<Station>>(emptyList())
        private set
    
    // 是否显示车站搜索框
    var showFromStationSearch by mutableStateOf(false)
        private set
    var showToStationSearch by mutableStateOf(false)
        private set
    
    init {
        loadSearchHistory()
    }
    
    // ==================== 查询操作 ====================
    
    /**
     * 查询车次
     */
    fun query(
        fromStationCode: String,
        toStationCode: String,
        departureDate: String
    ) {
        if (fromStationCode.isEmpty() || toStationCode.isEmpty()) {
            queryState = queryState.copy(error = "请选择出发站和到达站")
            return
        }
        
        if (departureDate.isEmpty()) {
            queryState = queryState.copy(error = "请选择出发日期")
            return
        }
        
        viewModelScope.launch {
            queryState = queryState.copy(isLoading = true, error = null)
            
            try {
                val query = com.ticket.grabber.data.model.TrainQuery(
                    fromStation = fromStationCode,
                    toStation = toStationCode,
                    departureDate = departureDate
                )
                
                val trains = trainRepository.queryTrainsWithAvailability(query)
                
                queryState = queryState.copy(
                    isLoading = false,
                    lastQuery = query
                )
                
                searchResult = SearchResult.Success(
                    trains = trains,
                    fromStationCode = fromStationCode,
                    toStationCode = toStationCode,
                    departureDate = departureDate
                )
                
                // 保存搜索历史
                saveSearchHistory(
                    fromStationCode = fromStationCode,
                    toStationCode = toStationCode,
                    departureDate = departureDate
                )
            } catch (e: Exception) {
                queryState = queryState.copy(
                    isLoading = false,
                    error = "查询失败: ${e.message}"
                )
                Log.e("SearchViewModel", "Query failed", e)
            }
        }
    }
    
    // ==================== 筛选和排序 ====================
    
    fun filterBySeatType(seatType: String) {
        searchResult = when (val result = searchResult) {
            is SearchResult.Success -> {
                SearchResult.Success(
                    trains = result.trains.filter { train ->
                        when (seatType) {
                            "business" -> train.businessSeat > 0
                            "first" -> train.firstClassSeat > 0
                            "second" -> train.secondClassSeat > 0
                            "hard" -> train.hardSeat > 0
                            "soft" -> train.softBed > 0
                            "hard_bed" -> train.hardBed > 0
                            else -> true
                        }
                    },
                    fromStationCode = result.fromStationCode,
                    toStationCode = result.toStationCode,
                    departureDate = result.departureDate
                )
            }
            else -> result
        }
    }
    
    fun sortTrains(sortType: SortType) {
        searchResult = when (val result = searchResult) {
            is SearchResult.Success -> {
                val sortedTrains = result.trains.sortedBy { train ->
                    when (sortType) {
                        SortType.TIME -> train.train.departureTime
                        SortType.DURATION -> train.train.duration
                        SortType.BUSINESS_SEAT -> train.businessSeat
                        SortType.FIRST_CLASS -> train.firstClassSeat
                        SortType.SECOND_CLASS -> train.secondClassSeat
                        SortType.HARD_SEAT -> train.hardSeat
                    }
                }
                
                SearchResult.Success(
                    trains = sortedTrains,
                    fromStationCode = result.fromStationCode,
                    toStationCode = result.toStationCode,
                    departureDate = result.departureDate
                )
            }
            else -> result
        }
    }
    
    // ==================== 车站选择 ====================
    
    fun selectFromStation(station: Station) {
        selectedFromStation = station
        fromStationKeyword = station.name
        showFromStationSearch = false
        queryState = queryState.copy(error = null)
    }
    
    fun selectToStation(station: Station) {
        selectedToStation = station
        toStationKeyword = station.name
        showToStationSearch = false
        queryState = queryState.copy(error = null)
    }
    
    fun showFromStationSelector() {
        showFromStationSearch = true
        showToStationSearch = false
    }
    
    fun showToStationSelector() {
        showToStationSearch = true
        showFromStationSearch = false
    }
    
    fun hideStationSearch() {
        showFromStationSearch = false
        showToStationSearch = false
    }
    
    fun searchStations(keyword: String, isFrom: Boolean) {
        if (keyword.isEmpty()) {
            if (isFrom) {
                fromStationSearchResult = emptyList()
            } else {
                toStationSearchResult = emptyList()
            }
            return
        }
        
        viewModelScope.launch {
            stationRepository.searchStations(keyword)
                .collect { stations ->
                    if (isFrom) {
                        fromStationSearchResult = stations
                    } else {
                        toStationSearchResult = stations
                    }
                }
        }
    }
    
    fun clearFromStation() {
        selectedFromStation = null
        fromStationKeyword = ""
    }
    
    fun clearToStation() {
        selectedToStation = null
        toStationKeyword = ""
    }
    
    // ==================== 搜索历史 ====================
    
    fun loadSearchHistory() {
        searchRepository.getSearchHistory()
            .collect { history ->
                searchHistory = history
            }
    }
    
    fun deleteSearchHistory(history: SearchHistory) {
        viewModelScope.launch {
            searchRepository.deleteSearchHistory(history)
        }
    }
    
    fun clearSearchHistory() {
        viewModelScope.launch {
            searchRepository.clearSearchHistory()
        }
    }
    
    private suspend fun saveSearchHistory(
        fromStationCode: String,
        toStationCode: String,
        departureDate: String
    ) {
        try {
            val fromStationName = stationRepository.getStationNameMap()[fromStationCode] ?: fromStationCode
            val toStationName = stationRepository.getStationNameMap()[toStationCode] ?: toStationCode
            
            val history = SearchHistory(
                fromStation = fromStationCode,
                fromStationName = fromStationName,
                toStation = toStationCode,
                toStationName = toStationName,
                departureDate = departureDate
            )
            searchRepository.saveSearchHistory(history)
        } catch (e: Exception) {
            Log.e("SearchViewModel", "Save search history failed", e)
        }
    }
}

/**
 * 查询状态
 */
data class QueryState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val lastQuery: com.ticket.grabber.data.model.TrainQuery? = null
)

/**
 * 搜索结果
 */
sealed interface SearchResult {
    data object Idle : SearchResult
    data class Success(
        val trains: List<TrainWithAvailability>,
        val fromStationCode: String,
        val toStationCode: String,
        val departureDate: String
    ) : SearchResult
}

/**
 * 排序类型
 */
enum class SortType {
    TIME,           // 按时间排序
    DURATION,       // 按时长排序
    BUSINESS_SEAT,  // 按商务座排序
    FIRST_CLASS,    // 按一等座排序
    SECOND_CLASS,   // 按二等座排序
    HARD_SEAT       // 按硬座排序
}
