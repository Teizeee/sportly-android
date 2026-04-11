package com.simple.sportly.data.remote.api.auth

import com.simple.sportly.data.remote.dto.auth.LoginRequestDto
import com.simple.sportly.data.remote.dto.auth.RegisterRequestDto
import com.simple.sportly.data.remote.dto.auth.TokenResponseDto
import com.simple.sportly.data.remote.dto.auth.UserResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequestDto): TokenResponseDto

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): UserResponseDto
}
