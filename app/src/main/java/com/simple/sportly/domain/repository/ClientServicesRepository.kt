package com.simple.sportly.domain.repository

import com.simple.sportly.domain.model.ClientActiveServices
import com.simple.sportly.domain.model.ClientBookings
import com.simple.sportly.domain.model.ClientMembership
import com.simple.sportly.domain.model.ClientProgress
import com.simple.sportly.domain.model.ClientTrainerPackage

interface ClientServicesRepository {
    suspend fun getMyActiveServices(): ClientActiveServices
    suspend fun getMyMemberships(): List<ClientMembership>
    suspend fun activateMembership(membershipId: String): ClientMembership
    suspend fun getMyPackages(): List<ClientTrainerPackage>
    suspend fun activatePackage(userTrainerPackageId: String): ClientTrainerPackage
    suspend fun getMyProgress(): List<ClientProgress>
    suspend fun getMyBookings(): ClientBookings
    suspend fun createMyBulkBookings(trainerSlotIds: List<String>): Int
    suspend fun cancelBooking(bookingId: String)
    suspend fun createMyProgress(weight: Double, height: Double, bmi: Double?): ClientProgress
}
