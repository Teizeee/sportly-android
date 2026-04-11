package com.simple.sportly.data.remote.api.profile

import com.simple.sportly.data.remote.dto.profile.UserProfileResponseDto
import com.simple.sportly.data.remote.dto.profile.UserUpdateRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileApi {
    @GET("api/v1/auth/me")
    suspend fun getMe(): UserProfileResponseDto

    @PUT("api/v1/auth/me")
    suspend fun updateMe(@Body request: UserUpdateRequestDto): UserProfileResponseDto
}
