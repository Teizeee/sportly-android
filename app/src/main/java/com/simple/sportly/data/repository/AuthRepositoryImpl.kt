package com.simple.sportly.data.repository

import com.simple.sportly.data.remote.api.AuthApi
import com.simple.sportly.data.remote.dto.LoginRequestDto
import com.simple.sportly.data.remote.dto.RegisterRequestDto
import com.simple.sportly.data.remote.dto.UserProfileResponseDto
import com.simple.sportly.data.remote.dto.UserUpdateRequestDto
import com.simple.sportly.domain.model.AuthSession
import com.simple.sportly.domain.model.UserProfile
import com.simple.sportly.domain.model.UserRole
import com.simple.sportly.domain.repository.AuthRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AuthRepositoryImpl(
    private val authApi: AuthApi
) : AuthRepository {
    override suspend fun login(email: String, password: String): AuthSession {
        val response = authApi.login(
            request = LoginRequestDto(
                email = email,
                password = password
            )
        )

        return AuthSession(
            token = response.accessToken,
            role = UserRole.fromApi(response.role)
        )
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        patronymic: String?,
        email: String,
        password: String
    ) {
        authApi.register(
            request = RegisterRequestDto(
                firstName = firstName,
                lastName = lastName,
                patronymic = patronymic,
                email = email,
                password = password,
                role = UserRole.CLIENT.name
            )
        )
    }

    override suspend fun getMyProfile(): UserProfile {
        return authApi.getMe().toDomain()
    }

    override suspend fun updateMyProfile(
        firstName: String,
        lastName: String,
        patronymic: String?,
        birthDate: String?,
        email: String,
        phone: String?,
        description: String?
    ): UserProfile {
        return authApi.updateMe(
            request = UserUpdateRequestDto(
                firstName = firstName,
                lastName = lastName,
                patronymic = patronymic,
                birthDate = birthDate,
                email = email,
                phone = phone,
                description = description
            )
        ).toDomain()
    }

    override suspend fun uploadAvatar(
        bytes: ByteArray,
        fileName: String,
        mimeType: String
    ) {
        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(
            name = "file",
            filename = fileName,
            body = requestBody
        )
        authApi.uploadAvatar(filePart)
    }

    private fun UserProfileResponseDto.toDomain(): UserProfile {
        return UserProfile(
            id = id,
            firstName = firstName,
            lastName = lastName,
            patronymic = patronymic,
            birthDate = birthDate,
            email = email,
            role = UserRole.fromApi(role),
            phone = trainerProfile?.phone,
            description = trainerProfile?.description
        )
    }
}
