package com.simple.sportly.data.repository

import com.simple.sportly.data.remote.api.client.ClientServicesApi
import com.simple.sportly.data.remote.dto.client.ActiveClientServicesDto
import com.simple.sportly.data.remote.dto.client.ClientMembershipDto
import com.simple.sportly.data.remote.dto.client.UserTrainerPackageDto
import com.simple.sportly.domain.model.ActiveMembership
import com.simple.sportly.domain.model.ActivePackage
import com.simple.sportly.domain.model.ClientActiveServices
import com.simple.sportly.domain.model.ClientMembership
import com.simple.sportly.domain.model.ClientMembershipStatus
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
}
