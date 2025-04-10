package com.dinajpur.app.DataClass

data class RegisterRequest(
    val name: String,
    val mobile: String,
    val email: String,
    val password: String,
    val password_confirmation: String,
    val gender: String,
    val location_id: Int
)