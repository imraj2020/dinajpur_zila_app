package com.dinajpur.app.DataClass

data class RegisterResponse(
    val message: String,
    val verifyCode: String,
    val userData: UserData
)

data class UserData(
    val id: Int,
    val name: String,
    val email: String,
    val mobile: String,
    val email_verified_at: String?,
    val verify_code: String,
    val verify_expires_at: String,
    val status: String,
    val gender: String,
    val role: String,
    val image: String?,
    val admin_id: String?,
    val manager_id: String?,
    val address: String?,
    val device_token: String?,
    val created_at: String,
    val updated_at: String
)
