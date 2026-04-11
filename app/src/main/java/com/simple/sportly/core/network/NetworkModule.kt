package com.simple.sportly.core.network

import com.simple.sportly.data.local.SessionStore
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    fun createRetrofit(
        baseUrl: String,
        sessionStore: SessionStore
    ): Retrofit {
        val headersInterceptor = Interceptor { chain ->
            val token = runBlocking { sessionStore.session.first()?.token }
            val requestBuilder = chain.request().newBuilder()
                .header("Subsystem", "mobile")

            if (!token.isNullOrBlank()) {
                requestBuilder.header("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }

        val unauthorizedInterceptor = Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            val path = request.url.encodedPath
            val isAuthRequest = path.endsWith("/auth/login") || path.endsWith("/auth/register")

            if (response.code == 401 && !isAuthRequest) {
                runBlocking { sessionStore.clearSession() }
            }

            response
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(headersInterceptor)
            .addInterceptor(unauthorizedInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
