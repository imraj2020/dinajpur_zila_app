package com.dinajpur.app.utils

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error") val errorMessage: String
)
