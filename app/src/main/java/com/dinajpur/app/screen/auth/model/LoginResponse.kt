package com.dinajpur.app.screen.auth.model
data class LoginResponse(
    val message: String,
    val access_token: String,
    val userData: UserData
)

data class UserData(
    val id: Int,
    val name: String,
    val email: String,
    val mobile: String,
    val email_verified_at: String?,
    val verify_code: String?,
    val verify_expires_at: String?,
    val status: String,
    val gender: String,
    val role: String,
    val image: String?,
    val admin_id: Int?,
    val manager_id: Int?,
    val address: String?,
    val device_token: String?,
    val created_at: String,
    val updated_at: String
)