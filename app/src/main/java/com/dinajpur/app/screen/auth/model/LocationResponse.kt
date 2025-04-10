package com.dinajpur.app.screen.auth.model

data class Location(
    val id: Int,
    val user_id: String,
    val location_name: String,
    val status: String,
    val created_at: String,
    val updated_at: String
)

data class LocationResponse(
    val message: String,
    val status_code: Int,
    val locationData: List<Location>
)