package com.simple.sportly.core.di

import android.content.Context
import com.simple.sportly.BuildConfig
import com.simple.sportly.core.network.NetworkModule
import com.simple.sportly.data.local.SessionStore
import com.simple.sportly.data.remote.api.auth.AuthApi
import com.simple.sportly.data.remote.api.client.ClientServicesApi
import com.simple.sportly.data.remote.api.gym.GymsApi
import com.simple.sportly.data.remote.api.gym.GymTrainersApi
import com.simple.sportly.data.remote.api.profile.AvatarApi
import com.simple.sportly.data.remote.api.profile.ProfileApi
import com.simple.sportly.data.remote.api.review.GymReviewsApi
import com.simple.sportly.data.remote.api.review.TrainerReviewsApi
import com.simple.sportly.data.remote.api.slots.TrainerSlotsApi
import com.simple.sportly.data.repository.AuthRepositoryImpl
import com.simple.sportly.data.repository.ClientServicesRepositoryImpl
import com.simple.sportly.data.repository.GymRepositoryImpl
import com.simple.sportly.data.repository.GymReviewsRepositoryImpl
import com.simple.sportly.data.repository.GymTrainersRepositoryImpl
import com.simple.sportly.data.repository.ProfileRepositoryImpl
import com.simple.sportly.data.repository.TrainerReviewsRepositoryImpl
import com.simple.sportly.data.repository.TrainerSlotsRepositoryImpl
import com.simple.sportly.domain.repository.AuthRepository
import com.simple.sportly.domain.repository.ClientServicesRepository
import com.simple.sportly.domain.repository.GymRepository
import com.simple.sportly.domain.repository.GymReviewsRepository
import com.simple.sportly.domain.repository.GymTrainersRepository
import com.simple.sportly.domain.repository.ProfileRepository
import com.simple.sportly.domain.repository.TrainerReviewsRepository
import com.simple.sportly.domain.repository.TrainerSlotsRepository

interface AppContainer {
    val sessionStore: SessionStore
    val authRepository: AuthRepository
    val profileRepository: ProfileRepository
    val clientServicesRepository: ClientServicesRepository
    val gymRepository: GymRepository
    val gymReviewsRepository: GymReviewsRepository
    val gymTrainersRepository: GymTrainersRepository
    val trainerReviewsRepository: TrainerReviewsRepository
    val trainerSlotsRepository: TrainerSlotsRepository
}

class DefaultAppContainer(
    appContext: Context
) : AppContainer {
    override val sessionStore: SessionStore = SessionStore(appContext)
    private val retrofit = NetworkModule.createRetrofit(
        baseUrl = BuildConfig.BASE_URL,
        sessionStore = sessionStore
    )
    private val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    private val profileApi: ProfileApi = retrofit.create(ProfileApi::class.java)
    private val clientServicesApi: ClientServicesApi = retrofit.create(ClientServicesApi::class.java)
    private val gymsApi: GymsApi = retrofit.create(GymsApi::class.java)
    private val gymTrainersApi: GymTrainersApi = retrofit.create(GymTrainersApi::class.java)
    private val gymReviewsApi: GymReviewsApi = retrofit.create(GymReviewsApi::class.java)
    private val avatarApi: AvatarApi = retrofit.create(AvatarApi::class.java)
    private val trainerReviewsApi: TrainerReviewsApi = retrofit.create(TrainerReviewsApi::class.java)
    private val trainerSlotsApi: TrainerSlotsApi = retrofit.create(TrainerSlotsApi::class.java)

    override val authRepository: AuthRepository = AuthRepositoryImpl(authApi)
    override val profileRepository: ProfileRepository = ProfileRepositoryImpl(profileApi, avatarApi)
    override val clientServicesRepository: ClientServicesRepository =
        ClientServicesRepositoryImpl(clientServicesApi)
    override val gymRepository: GymRepository = GymRepositoryImpl(gymsApi)
    override val gymReviewsRepository: GymReviewsRepository =
        GymReviewsRepositoryImpl(gymReviewsApi)
    override val gymTrainersRepository: GymTrainersRepository =
        GymTrainersRepositoryImpl(gymTrainersApi)
    override val trainerReviewsRepository: TrainerReviewsRepository =
        TrainerReviewsRepositoryImpl(trainerReviewsApi)
    override val trainerSlotsRepository: TrainerSlotsRepository =
        TrainerSlotsRepositoryImpl(trainerSlotsApi)
}
