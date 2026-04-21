package com.ticket.grabber.data.api.auth

import com.google.gson.annotations.SerializedName

/**
 * 用户登录请求
 */
data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("captcha") val captcha: String? = null,
    @SerializedName("uuid") val uuid: String? = null
)

/**
 * 验证码响应
 */
data class CaptchaResponse(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("img") val img: String?,
    @SerializedName("isCaptchaRequired") val isCaptchaRequired: Boolean
)

/**
 * 用户信息
 */
data class UserInfo(
    @SerializedName("userId") val userId: String,
    @SerializedName("username") val username: String,
    @SerializedName("nickName") val nickName: String?,
    @SerializedName("realName") val realName: String?,
    @SerializedName("idType") val idType: String?,
    @SerializedName("idNo") val idNo: String?,
    @SerializedName("phoneNo") val phoneNo: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("isVerified") val isVerified: Boolean,
    @SerializedName("verifyStatus") val verifyStatus: Int,
    @SerializedName("createTime") val createTime: Long = System.currentTimeMillis()
)

/**
 * 登录响应
 */
data class LoginResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("msg") val msg: String,
    @SerializedName("isCaptchaRequired") val isCaptchaRequired: Boolean,
    @SerializedName("uuid") val uuid: String? = null,
    @SerializedName("userInfo") val userInfo: UserInfo? = null
)

/**
 * 登出响应
 */
data class LogoutResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("msg") val msg: String
)

/**
 * 认证状态响应
 */
data class AuthStatusResponse(
    @SerializedName("isLogin") val isLogin: Boolean,
    @SerializedName("userInfo") val userInfo: UserInfo? = null
)
