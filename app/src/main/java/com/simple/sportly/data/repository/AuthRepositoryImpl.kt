package com.simple.sportly.data.repository

import com.simple.sportly.data.remote.api.auth.AuthApi
import com.simple.sportly.data.remote.dto.auth.LoginRequestDto
import com.simple.sportly.data.remote.dto.auth.RegisterRequestDto
import com.simple.sportly.domain.model.AuthSession
import com.simple.sportly.domain.model.UserRole
import com.simple.sportly.domain.repository.AuthRepository

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
}
