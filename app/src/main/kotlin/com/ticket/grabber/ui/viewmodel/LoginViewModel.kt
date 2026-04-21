package com.ticket.grabber.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ticket.grabber.base.UiState
import com.ticket.grabber.data.api.auth.AuthRepository
import com.ticket.grabber.data.model.AuthSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 登录状态
 */
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val session: AuthSession) : LoginState()
    data class Error(val message: String) : LoginState()
}

/**
 * 登录表单状态
 */
data class LoginForm(
    val username: String = "",
    val password: String = "",
    val captcha: String = "",
    val captchaUuid: String? = null,
    val showCaptcha: Boolean = false
)

/**
 * 登录ViewModel
 * 处理用户登录逻辑和验证码管理
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    // 登录表单状态
    private val _formState = MutableStateFlow(LoginForm())
    val formState: StateFlow<LoginForm> = _formState.asStateFlow()
    
    // 登录状态
    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val loginState: StateFlow<UiState<Unit>> = _loginState.asStateFlow()
    
    // 验证码图片URL
    private val _captchaImage = MutableStateFlow<String?>(null)
    val captchaImage: StateFlow<String?> = _captchaImage.asStateFlow()
    
    // 是否需要验证码
    private val _requireCaptcha = MutableStateFlow(false)
    val requireCaptcha: StateFlow<Boolean> = _requireCaptcha.asStateFlow()
    
    // 验证码UUID
    private val _captchaUuid = MutableStateFlow<String?>(null)
    val captchaUuid: StateFlow<String?> = _captchaUuid.asStateFlow()
    
    // 登录会话
    private val _session = MutableStateFlow<AuthSession?>(null)
    val session: StateFlow<AuthSession?> = _session.asStateFlow()
    
    init {
        viewModelScope.launch {
            // 加载当前会话
            authRepository.getAuthSession().collect { newSession ->
                _session.value = newSession
                if (newSession?.isLogin == true) {
                    _loginState.value = UiState.Success(Unit)
                }
            }
        }
        
        // 获取验证码
        refreshCaptcha()
    }
    
    /**
     * 更新表单字段
     */
    fun updateField(field: String, value: String) {
        _formState.update { current ->
            when (field) {
                "username" -> current.copy(username = value)
                "password" -> current.copy(password = value)
                "captcha" -> current.copy(captcha = value)
                else -> current
            }
        }
    }
    
    /**
     * 获取验证码
     */
    fun refreshCaptcha() {
        viewModelScope.launch {
            _loginState.value = UiState.Loading
            val response = authRepository.getCaptchaImage()
            
            when {
                response.isCaptchaRequired && response.uuid.isNotEmpty() -> {
                    _requireCaptcha.value = true
                    _captchaUuid.value = response.uuid
                    _captchaImage.value = response.img
                    _formState.update { it.copy(showCaptcha = true, captchaUuid = response.uuid) }
                    _loginState.value = UiState.Success(Unit)
                }
                response.uuid.isNotEmpty() -> {
                    _requireCaptcha.value = false
                    _captchaUuid.value = response.uuid
                    _formState.update { it.copy(showCaptcha = false, captchaUuid = response.uuid) }
                    _loginState.value = UiState.Success(Unit)
                }
                else -> {
                    _loginState.value = UiState.Error("获取验证码失败: ${response.uuid}")
                }
            }
        }
    }
    
    /**
     * 用户登录
     */
    fun login() {
        viewModelScope.launch {
            val form = _formState.value
            
            // 表单验证
            if (form.username.isEmpty()) {
                _loginState.value = UiState.Error("请输入用户名")
                return@launch
            }
            
            if (form.password.isEmpty()) {
                _loginState.value = UiState.Error("请输入密码")
                return@launch
            }
            
            if (form.showCaptcha && form.captcha.isEmpty()) {
                _loginState.value = UiState.Error("请输入验证码")
                return@launch
            }
            
            _loginState.value = UiState.Loading
            
            val response = authRepository.login(
                username = form.username,
                password = form.password,
                captcha = form.captcha.takeIf { form.showCaptcha },
                uuid = form.captchaUuid
            )
            
            when {
                response.status == 1 -> {
                    // 登录成功
                    val authSession = AuthSession(
                        username = form.username,
                        nickName = response.userInfo?.nickName,
                        isLogin = true,
                        sessionId = response.uuid,
                        cookies = null
                    )
                    
                    authRepository.saveLoginState(authSession)
                    _session.value = authSession
                    _loginState.value = UiState.Success(Unit)
                }
                response.status == -2 || response.isCaptchaRequired -> {
                    // 需要验证码
                    _requireCaptcha.value = true
                    _captchaUuid.value = response.uuid
                    _formState.update { it.copy(showCaptcha = true, captchaUuid = response.uuid) }
                    _loginState.value = UiState.Error(response.msg)
                }
                else -> {
                    // 登录失败
                    _loginState.value = UiState.Error(response.msg)
                }
            }
        }
    }
    
    /**
     * 用户登出
     */
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _session.value = null
            _formState.value = LoginForm()
            _loginState.value = UiState.Success(Unit)
        }
    }
    
    /**
     * 检查登录状态
     */
    fun checkAuthStatus() {
        viewModelScope.launch {
            val response = authRepository.checkAuthStatus()
            
            if (response.isLogin && response.userInfo != null) {
                val authSession = AuthSession(
                    username = response.userInfo.username,
                    nickName = response.userInfo.nickName,
                    isLogin = true,
                    cookies = null
                )
                
                authRepository.saveLoginState(authSession)
                _session.value = authSession
                _loginState.value = UiState.Success(Unit)
            } else {
                _loginState.value = UiState.Error("未登录")
            }
        }
    }
    
    /**
     * 清除表单错误
     */
    fun clearError() {
        _loginState.value = UiState.Success(Unit)
    }
}
