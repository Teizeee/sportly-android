package com.simple.sportly.data.repository

import com.simple.sportly.data.remote.api.profile.AvatarApi
import com.simple.sportly.data.remote.api.profile.ProfileApi
import com.simple.sportly.data.remote.dto.profile.UserProfileResponseDto
import com.simple.sportly.data.remote.dto.profile.UserUpdateRequestDto
import com.simple.sportly.domain.model.UserProfile
import com.simple.sportly.domain.model.UserRole
import com.simple.sportly.domain.repository.ProfileRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProfileRepositoryImpl(
    private val profileApi: ProfileApi,
    private val avatarApi: AvatarApi
) : ProfileRepository {
    override suspend fun getMyProfile(): UserProfile {
        return profileApi.getMe().toDomain()
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
        return profileApi.updateMe(
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
        avatarApi.uploadAvatar(filePart)
    }

    private fun UserProfileResponseDto.toDomain(): UserProfile {
        return UserProfile(
            id = id,
            trainerId = trainerProfile?.id,
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
