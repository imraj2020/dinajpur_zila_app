package com.dinajpur.app.screen.auth.model

data class RegistrationRequest(
    val name: String,
    val mobile: String,
    val email: String,
    val password: String,
    val password_confirmation: String,
    val gender: String,
    val location_id: Int
)
// Response model
data class RegistrationResponse(
    val message: String,
    val verifyCode: String,
    val userData: UserData
)

