package com.simple.sportly.data.remote.api

import com.simple.sportly.data.remote.dto.LoginRequestDto
import com.simple.sportly.data.remote.dto.RegisterRequestDto
import com.simple.sportly.data.remote.dto.TokenResponseDto
import com.simple.sportly.data.remote.dto.UserProfileResponseDto
import com.simple.sportly.data.remote.dto.UserResponseDto
import com.simple.sportly.data.remote.dto.UserUpdateRequestDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface AuthApi {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequestDto): TokenResponseDto

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): UserResponseDto

    @GET("api/v1/auth/me")
    suspend fun getMe(): UserProfileResponseDto

    @PUT("api/v1/auth/me")
    suspend fun updateMe(@Body request: UserUpdateRequestDto): UserProfileResponseDto

    @Multipart
    @POST("api/v1/avatars/")
    suspend fun uploadAvatar(@Part file: MultipartBody.Part)
}
