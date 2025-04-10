package com.dinajpur.app.network

import com.dinajpur.app.DataClass.RegisterRequest
import com.dinajpur.app.DataClass.RegisterResponse
import com.dinajpur.app.screen.auth.model.LoginRequest
import com.dinajpur.app.screen.auth.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    // 🔹 Login API
    @POST("api/login")
    fun login(@Body loginReq: LoginRequest): Call<LoginResponse>



        @Headers("Accept: application/json")
        @POST("api/register")
        fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>


}