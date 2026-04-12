package com.simple.sportly.data.repository

import com.simple.sportly.data.remote.api.client.ClientServicesApi
import com.simple.sportly.data.remote.dto.client.ActiveClientServicesDto
import com.simple.sportly.data.remote.dto.client.BookingBulkCreateItemDto
import com.simple.sportly.data.remote.dto.client.ClientBookingItemDto
import com.simple.sportly.data.remote.dto.client.ClientBookingsDto
import com.simple.sportly.data.remote.dto.client.ClientMembershipDto
import com.simple.sportly.data.remote.dto.client.ClientProgressCreateDto
import com.simple.sportly.data.remote.dto.client.ClientProgressDto
import com.simple.sportly.data.remote.dto.client.UserTrainerPackageDto
import com.simple.sportly.domain.model.ActiveMembership
import com.simple.sportly.domain.model.ActivePackage
import com.simple.sportly.domain.model.ClientActiveServices
import com.simple.sportly.domain.model.ClientBooking
import com.simple.sportly.domain.model.ClientBookingStatus
import com.simple.sportly.domain.model.ClientBookings
import com.simple.sportly.domain.model.ClientMembership
import com.simple.sportly.domain.model.ClientMembershipStatus
import com.simple.sportly.domain.model.ClientProgress
import com.simple.sportly.domain.model.ClientTrainerPackage
import com.simple.sportly.domain.model.ClientTrainerPackageStatus
import com.simple.sportly.domain.repository.ClientServicesRepository

class ClientServicesRepositoryImpl(
    private val clientServicesApi: ClientServicesApi
) : ClientServicesRepository {
    override suspend fun getMyActiveServices(): ClientActiveServices {
        return clientServicesApi.getMyActiveServices().toDomain()
    }

    override suspend fun getMyMemberships(): List<ClientMembership> {
        return clientServicesApi.getMyMemberships().map { it.toDomain() }
    }

    override suspend fun activateMembership(membershipId: String): ClientMembership {
        return clientServicesApi.activateMembership(membershipId).toDomain()
    }

    override suspend fun getMyPackages(): List<ClientTrainerPackage> {
        return clientServicesApi.getMyPackages().map { it.toDomain() }
    }

    override suspend fun activatePackage(userTrainerPackageId: String): ClientTrainerPackage {
        return clientServicesApi.activatePackage(userTrainerPackageId).toDomain()
    }

    override suspend fun getMyProgress(): List<ClientProgress> {
        return clientServicesApi.getMyProgress().map { it.toDomain() }
    }

    override suspend fun getMyBookings(): ClientBookings {
        return clientServicesApi.getMyBookings().toDomain()
    }

    override suspend fun createMyBulkBookings(trainerSlotIds: List<String>): Int {
        return clientServicesApi.createMyBulkBookings(
            payload = trainerSlotIds.map { trainerSlotId ->
                BookingBulkCreateItemDto(trainerSlotId = trainerSlotId)
            }
        ).size
    }

    override suspend fun cancelBooking(bookingId: String) {
        clientServicesApi.cancelBooking(bookingId)
    }

    override suspend fun createMyProgress(weight: Double, height: Double, bmi: Double?): ClientProgress {
        return clientServicesApi.createMyProgress(
            request = ClientProgressCreateDto(
                weight = weight,
                height = height,
                bmi = bmi
            )
        ).toDomain()
    }

    private fun ActiveClientServicesDto.toDomain(): ClientActiveServices {
        return ClientActiveServices(
            activeMembership = activeMembership?.toActiveDomain(),
            activePackage = activePackage?.toActiveDomain()
        )
    }

    private fun ClientMembershipDto.toActiveDomain(): ActiveMembership {
        return ActiveMembership(
            id = id,
            membershipTypeId = membershipTypeId,
            membershipTypeName = service.name,
            membershipDescription = service.description,
            durationMonths = service.durationMonths,
            status = status,
            expiresAt = expiresAt
        )
    }

    private fun UserTrainerPackageDto.toActiveDomain(): ActivePackage {
        val trainerFirstName = service.trainer?.user?.firstName
        val trainerLastName = service.trainer?.user?.lastName
        val trainerPatronymic = service.trainer?.user?.patronymic
        return ActivePackage(
            id = id,
            trainerPackageId = trainerPackageId,
            trainerId = service.trainer?.id,
            trainerPackageName = service.name,
            trainerPackageDescription = service.description,
            trainerPackageSessionCount = service.sessionCount,
            trainerFirstName = trainerFirstName,
            trainerLastName = trainerLastName,
            trainerPatronymic = trainerPatronymic,
            status = status,
            sessionsLeft = sessionsLeft
        )
    }

    private fun ClientMembershipDto.toDomain(): ClientMembership {
        return ClientMembership(
            id = id,
            membershipTypeId = membershipTypeId,
            membershipTypeName = service.name,
            membershipDescription = service.description,
            durationMonths = service.durationMonths,
            status = ClientMembershipStatus.fromApi(status),
            purchasedAt = purchasedAt,
            activatedAt = activatedAt,
            expiresAt = expiresAt
        )
    }

    private fun UserTrainerPackageDto.toDomain(): ClientTrainerPackage {
        return ClientTrainerPackage(
            id = id,
            trainerPackageId = trainerPackageId,
            trainerId = service.trainer?.id,
            packageName = service.name,
            packageDescription = service.description,
            sessionCount = service.sessionCount,
            trainerFirstName = service.trainer?.user?.firstName,
            trainerLastName = service.trainer?.user?.lastName,
            trainerPatronymic = service.trainer?.user?.patronymic,
            status = ClientTrainerPackageStatus.fromApi(status),
            sessionsLeft = sessionsLeft,
            purchasedAt = purchasedAt,
            activatedAt = activatedAt,
            expiresAt = expiresAt
        )
    }

    private fun ClientProgressDto.toDomain(): ClientProgress {
        return ClientProgress(
            id = id,
            userId = userId,
            weight = weight.toDoubleOrNull() ?: 0.0,
            height = height.toDoubleOrNull() ?: 0.0,
            bmi = bmi.toDoubleOrNull() ?: 0.0,
            recordedAt = recordedAt
        )
    }

    private fun ClientBookingsDto.toDomain(): ClientBookings {
        return ClientBookings(
            upcoming = upcoming.map { it.toDomain() },
            past = past.map { it.toDomain() }
        )
    }

    private fun ClientBookingItemDto.toDomain(): ClientBooking {
        return ClientBooking(
            id = id,
            status = ClientBookingStatus.fromApi(status),
            date = date,
            startTime = startTime,
            endTime = endTime,
            gymId = gym.id,
            gymTitle = gym.title,
            trainerId = trainer.id,
            trainerFirstName = trainer.firstName,
            trainerLastName = trainer.lastName,
            trainerPatronymic = trainer.patronymic
        )
    }
}
