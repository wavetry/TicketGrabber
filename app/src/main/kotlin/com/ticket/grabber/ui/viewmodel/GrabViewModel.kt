package com.ticket.grabber.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ticket.grabber.data.model.*
import com.ticket.grabber.data.repository.GrabResultDao
import com.ticket.grabber.data.repository.GrabTaskRepository
import com.ticket.grabber.data.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 抢票任务UI状态
 */
data class GrabTaskUiState(
    val tasks: List<GrabTask> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedTask: GrabTask? = null,
    val createTaskForm: CreateTaskForm = CreateTaskForm()
)

/**
 * 创建任务表单
 */
data class CreateTaskForm(
    val trainNo: String = "",
    val trainCode: String = "",
    val fromStation: String = "",
    val toStation: String = "",
    val departureDate: String = "",
    val selectedSeatTypes: List<String> = emptyList(),
    val selectedPassengerIds: List<Long> = emptyList(),
    val priority: Int = 0,
    val maxRetryCount: Int = 100,
    val queryInterval: Long = 3000
)

/**
 * 抢票结果UI状态
 */
data class GrabResultUiState(
    val results: List<GrabResult> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val orderNo: String? = null
)

/**
 * 抢票ViewModel
 */
@HiltViewModel
class GrabViewModel @Inject constructor(
    private val grabTaskRepository: GrabTaskRepository,
    private val ticketRepository: TicketRepository,
    private val resultDao: GrabResultDao
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(GrabTaskUiState())
    val uiState: StateFlow<GrabTaskUiState> = _uiState.asStateFlow()
    
    init {
        loadTasks()
    }
    
    /**
     * 加载抢票任务列表
     */
    fun loadTasks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            grabTaskRepository.getTasks()
                .catch { e ->
                    _uiState.update { 
                        it.copy(isLoading = false, error = e.message) 
                    }
                }
                .collect { tasks ->
                    _uiState.update { 
                        it.copy(tasks = tasks, isLoading = false) 
                    }
                }
        }
    }
    
    /**
     * 创建抢票任务
     */
    fun createTask() {
        val form = _uiState.value.createTaskForm
        
        // 验证表单
        if (form.trainNo.isBlank() || form.fromStation.isBlank() || 
            form.toStation.isBlank() || form.departureDate.isBlank() ||
            form.selectedSeatTypes.isEmpty() || form.selectedPassengerIds.isEmpty()) {
            _uiState.update { it.copy(error = "请填写完整的抢票信息") }
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val request = CreateGrabTaskRequest(
                trainNo = form.trainNo,
                trainCode = form.trainCode,
                fromStation = form.fromStation,
                toStation = form.toStation,
                departureDate = form.departureDate,
                seatTypes = form.selectedSeatTypes,
                passengerIds = form.selectedPassengerIds,
                priority = form.priority,
                maxRetryCount = form.maxRetryCount,
                queryInterval = form.queryInterval
            )
            
            grabTaskRepository.createTask(request)
                .onSuccess { task ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            createTaskForm = CreateTaskForm() // 重置表单
                        )
                    }
                    // 自动启动任务
                    startTask(task.taskId)
                }
                .onFailure { e ->
                    _uiState.update { 
                        it.copy(isLoading = false, error = e.message) 
                    }
                }
        }
    }
    
    /**
     * 启动抢票任务
     */
    fun startTask(taskId: String) {
        viewModelScope.launch {
            grabTaskRepository.startTask(taskId)
                .onSuccess { task ->
                    updateTaskInList(task)
                    startTicketMonitoring(task)
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }
    
    /**
     * 停止抢票任务
     */
    fun stopTask(taskId: String) {
        viewModelScope.launch {
            grabTaskRepository.stopTask(taskId)
                .onSuccess { task ->
                    updateTaskInList(task)
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }
    
    /**
     * 取消抢票任务
     */
    fun cancelTask(taskId: String) {
        viewModelScope.launch {
            grabTaskRepository.cancelTask(taskId)
                .onSuccess { task ->
                    updateTaskInList(task)
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }
    
    /**
     * 删除抢票任务
     */
    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            grabTaskRepository.deleteTask(taskId)
                .onSuccess {
                    loadTasks() // 刷新列表
                }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }
    
    /**
     * 更新表单字段
     */
    fun updateForm(update: (CreateTaskForm) -> CreateTaskForm) {
        _uiState.update { 
            it.copy(createTaskForm = update(it.createTaskForm)) 
        }
    }
    
    /**
     * 选择车次
     */
    fun selectTrain(train: Train) {
        _uiState.update {
            it.copy(
                createTaskForm = it.createTaskForm.copy(
                    trainNo = train.trainNo,
                    trainCode = train.trainCode,
                    fromStation = train.fromStation,
                    toStation = train.toStation
                )
            )
        }
    }
    
    /**
     * 选择席别
     */
    fun toggleSeatType(seatType: String) {
        _uiState.update { state ->
            val currentTypes = state.createTaskForm.selectedSeatTypes
            val newTypes = if (currentTypes.contains(seatType)) {
                currentTypes - seatType
            } else {
                currentTypes + seatType
            }
            state.copy(
                createTaskForm = state.createTaskForm.copy(
                    selectedSeatTypes = newTypes
                )
            )
        }
    }
    
    /**
     * 选择乘客
     */
    fun togglePassenger(passengerId: Long) {
        _uiState.update { state ->
            val currentIds = state.createTaskForm.selectedPassengerIds
            val newIds = if (currentIds.contains(passengerId)) {
                currentIds - passengerId
            } else {
                currentIds + passengerId
            }
            state.copy(
                createTaskForm = state.createTaskForm.copy(
                    selectedPassengerIds = newIds
                )
            )
        }
    }
    
    /**
     * 清除错误
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    
    /**
     * 更新列表中的任务
     */
    private fun updateTaskInList(updatedTask: GrabTask) {
        _uiState.update { state ->
            val newTasks = state.tasks.map { 
                if (it.taskId == updatedTask.taskId) updatedTask else it 
            }
            state.copy(tasks = newTasks)
        }
    }
    
    /**
     * 开始余票监控（核心抢票逻辑）
     */
    private fun startTicketMonitoring(task: GrabTask) {
        viewModelScope.launch {
            var retryCount = 0
            
            while (retryCount < task.maxRetryCount) {
                // 检查任务状态
                val currentTask = grabTaskRepository.getTask(task.taskId)
                if (currentTask.isSuccess && 
                    currentTask.getOrNull()?.status != GrabTaskStatus.RUNNING.code) {
                    break
                }
                
                // 查询余票
                val availabilityResult = ticketRepository.checkTicketAvailability(
                    task.trainNo,
                    task.fromStation,
                    task.toStation,
                    task.departureDate
                )
                
                if (availabilityResult.isSuccess) {
                    val availability = availabilityResult.getOrNull()
                    if (availability?.hasTickets == true) {
                        // 检查是否有目标席别有票
                        val availableSeat = task.seatTypes.split(",").firstOrNull { 
                            availability.seatAvailability[it] == true 
                        }
                        
                        if (availableSeat != null) {
                            // 有余票，尝试提交订单
                            submitOrder(task, availableSeat)
                            return@launch
                        }
                    }
                }
                
                // 增加重试次数
                retryCount++
                grabTaskRepository.incrementRetryCount(task.taskId)
                
                // 更新任务状态
                grabTaskRepository.updateTaskStatus(
                    task.taskId,
                    GrabTaskStatus.RUNNING,
                    errorMessage = "已尝试 ${retryCount}/${task.maxRetryCount} 次"
                )
                
                // 记录结果
                recordResult(task.taskId, false, null, "查询余票: ${retryCount}/${task.maxRetryCount}")
                
                // 等待下次查询
                delay(task.queryInterval)
            }
            
            // 超过最大重试次数
            if (retryCount >= task.maxRetryCount) {
                grabTaskRepository.updateTaskStatus(
                    task.taskId,
                    GrabTaskStatus.FAILED,
                    errorMessage = "超过最大重试次数"
                )
                recordResult(task.taskId, false, null, "超过最大重试次数")
            }
        }
    }
    
    /**
     * 提交订单
     */
    private suspend fun submitOrder(task: GrabTask, seatType: String) {
        val passengers = task.passengers.split(",").map { id ->
            PassengerInfo(name = "乘客$id", idNo = "110101199001011234", seatType = seatType)
        }
        
        val result = ticketRepository.submitOrder(
            trainNo = task.trainNo,
            fromStation = task.fromStation,
            toStation = task.toStation,
            departureDate = task.departureDate,
            passengers = passengers,
            seatType = seatType
        )
        
        if (result.isSuccess) {
            val order = result.getOrNull()
            grabTaskRepository.updateTaskStatus(
                task.taskId,
                GrabTaskStatus.SUCCESS,
                orderNo = order?.orderNo,
                errorMessage = null
            )
            recordResult(task.taskId, true, order?.orderNo, "抢票成功")
        } else {
            grabTaskRepository.updateTaskStatus(
                task.taskId,
                GrabTaskStatus.FAILED,
                errorMessage = result.exceptionOrNull()?.message
            )
            recordResult(task.taskId, false, null, "提交订单失败: ${result.exceptionOrNull()?.message}")
        }
    }
    
    /**
     * 记录抢票结果
     */
    private suspend fun recordResult(
        taskId: String,
        success: Boolean,
        orderNo: String?,
        message: String
    ) {
        val result = GrabResult(
            taskId = taskId,
            success = success,
            orderNo = orderNo,
            message = message
        )
        resultDao.insert(result)
    }
}
