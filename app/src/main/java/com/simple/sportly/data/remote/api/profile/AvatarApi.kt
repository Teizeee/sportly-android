package com.simple.sportly.data.remote.api.profile

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AvatarApi {
    @Multipart
    @POST("api/v1/avatars/")
    suspend fun uploadAvatar(@Part file: MultipartBody.Part)
}
