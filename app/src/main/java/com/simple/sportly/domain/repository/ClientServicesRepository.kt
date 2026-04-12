package com.simple.sportly.domain.repository

import com.simple.sportly.domain.model.ClientActiveServices
import com.simple.sportly.domain.model.ClientMembership
import com.simple.sportly.domain.model.ClientTrainerPackage

interface ClientServicesRepository {
    suspend fun getMyActiveServices(): ClientActiveServices
    suspend fun getMyMemberships(): List<ClientMembership>
    suspend fun activateMembership(membershipId: String): ClientMembership
    suspend fun getMyPackages(): List<ClientTrainerPackage>
    suspend fun activatePackage(userTrainerPackageId: String): ClientTrainerPackage
}
