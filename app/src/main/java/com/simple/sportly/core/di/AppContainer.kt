package com.simple.sportly.core.di

import android.content.Context
import com.simple.sportly.BuildConfig
import com.simple.sportly.core.network.NetworkModule
import com.simple.sportly.data.local.SessionStore
import com.simple.sportly.data.remote.api.AuthApi
import com.simple.sportly.data.repository.AuthRepositoryImpl
import com.simple.sportly.domain.repository.AuthRepository

interface AppContainer {
    val sessionStore: SessionStore
    val authRepository: AuthRepository
}

class DefaultAppContainer(
    appContext: Context
) : AppContainer {
    override val sessionStore: SessionStore = SessionStore(appContext)
    private val authApi: AuthApi = NetworkModule.createAuthApi(
        baseUrl = BuildConfig.BASE_URL,
        sessionStore = sessionStore
    )
    override val authRepository: AuthRepository = AuthRepositoryImpl(authApi)
}
