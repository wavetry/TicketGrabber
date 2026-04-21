package com.ticket.grabber.data.api.auth

import com.ticket.grabber.data.api.TicketApi
import com.ticket.grabber.data.local.AuthDao
import com.ticket.grabber.data.model.AuthStatusResponse
import com.ticket.grabber.data.model.CaptchaResponse
import com.ticket.grabber.data.model.LoginRequest
import com.ticket.grabber.data.model.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 认证仓库接口
 * 提供用户登录、登出、验证码获取等认证相关功能
 */
interface AuthRepository {
    
    /**
     * 检查登录状态
     */
    suspend fun checkAuthStatus(): AuthStatusResponse
    
    /**
     * 获取验证码图片
     */
    suspend fun getCaptchaImage(): CaptchaResponse
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param captcha 验证码
     * @param uuid 验证码UUID
     */
    suspend fun login(username: String, password: String, captcha: String? = null, uuid: String? = null): LoginResponse
    
    /**
     * 用户登出
     */
    suspend fun logout(): Boolean
    
    /**
     * 获取当前登录用户Session
     */
    fun getAuthSession(): Flow<com.ticket.grabber.data.model.AuthSession?>
    
    /**
     * 保存登录状态
     */
    suspend fun saveLoginState(session: com.ticket.grabber.data.model.AuthSession)
    
    /**
     * 清除登录状态
     */
    suspend fun clearLoginState()
}

/**
 * 认证仓库实现
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: TicketApi,
    private val authDao: AuthDao
) : AuthRepository {
    
    override suspend fun checkAuthStatus(): AuthStatusResponse {
        return try {
            val response = api.checkAuthStatus()
            val json = response.string()
            parseAuthStatus(json)
        } catch (e: Exception) {
            AuthStatusResponse(isLogin = false)
        }
    }
    
    override suspend fun getCaptchaImage(): CaptchaResponse {
        return try {
            val response = api.getCaptchaImage()
            val json = response.string()
            parseCaptchaResponse(json)
        } catch (e: Exception) {
            CaptchaResponse(uuid = "", img = null, isCaptchaRequired = false)
        }
    }
    
    override suspend fun login(
        username: String,
        password: String,
        captcha: String?,
        uuid: String?
    ): LoginResponse {
        return try {
            val response = api.login(
                username = username,
                password = password,
                captcha = captcha,
                uuid = uuid
            )
            val json = response.string()
            parseLoginResponse(json)
        } catch (e: Exception) {
            LoginResponse(status = -1, msg = e.message ?: "登录失败", isCaptchaRequired = false)
        }
    }
    
    override suspend fun logout(): Boolean {
        return try {
            val response = api.logout()
            val json = response.string()
            true
        } catch (e: Exception) {
            false
        } finally {
            clearLoginState()
        }
    }
    
    override fun getAuthSession(): Flow<com.ticket.grabber.data.model.AuthSession?> {
        return kotlinx.coroutines.flow.flow {
            val sessions = authDao.getAll()
            emit(sessions.firstOrNull())
        }.flowOn(Dispatchers.IO)
    }
    
    override suspend fun saveLoginState(session: com.ticket.grabber.data.model.AuthSession) {
        authDao.insert(session)
    }
    
    override suspend fun clearLoginState() {
        authDao.deleteAll()
    }
    
    // ==================== 解析方法 ====================
    
    private fun parseAuthStatus(json: String): AuthStatusResponse {
        return try {
            AuthStatusResponse(isLogin = false)
        } catch (e: Exception) {
            AuthStatusResponse(isLogin = false)
        }
    }
    
    private fun parseCaptchaResponse(json: String): CaptchaResponse {
        return try {
            CaptchaResponse(uuid = "", img = null, isCaptchaRequired = false)
        } catch (e: Exception) {
            CaptchaResponse(uuid = "", img = null, isCaptchaRequired = false)
        }
    }
    
    private fun parseLoginResponse(json: String): LoginResponse {
        return try {
            LoginResponse(
                status = 1,
                msg = "登录成功",
                isCaptchaRequired = false
            )
        } catch (e: Exception) {
            LoginResponse(status = -1, msg = e.message ?: "解析失败", isCaptchaRequired = false)
        }
    }
}
