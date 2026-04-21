package com.ticket.grabber.data.model

/**
 * 用户登录请求
 */
data class LoginRequest(
    val username: String,
    val password: String,
    val captcha: String? = null,
    val uuid: String? = null
)

/**
 * 验证码响应
 */
data class CaptchaResponse(
    val uuid: String,
    val img: String?, // Base64 image or null if empty
    val isCaptchaRequired: Boolean
)

/**
 * 用户信息
 */
data class UserInfo(
    val userId: String,
    val username: String,
    val nickName: String?,
    val realName: String?,
    val idType: String?,
    val idNo: String?,
    val phoneNo: String?,
    val email: String?,
    val address: String?,
    val isVerified: Boolean,
    val verifyStatus: Int,
    val createTime: Long = System.currentTimeMillis()
)

/**
 * 登录响应
 */
data class LoginResponse(
    val status: Int,
    val msg: String,
    val isCaptchaRequired: Boolean,
    val uuid: String? = null,
    val userInfo: UserInfo? = null
)

/**
 * 登出响应
 */
data class LogoutResponse(
    val status: Int,
    val msg: String
)

/**
 * 认证状态响应
 */
data class AuthStatusResponse(
    val isLogin: Boolean,
    val userInfo: UserInfo? = null
)

/**
 * 本地认证信息（用于持久化）
 */
data class AuthSession(
    val id: Int = 1,
    val username: String,
    val nickName: String?,
    val isLogin: Boolean,
    val loginTime: Long = System.currentTimeMillis(),
    val lastRefreshTime: Long = System.currentTimeMillis(),
    val sessionId: String? = null,
    val cookies: String? = null //Serialized cookies for persistence
)
