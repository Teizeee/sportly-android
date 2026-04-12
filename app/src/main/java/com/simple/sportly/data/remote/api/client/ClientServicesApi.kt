package com.simple.sportly.data.remote.api.client

import com.simple.sportly.data.remote.dto.client.ActiveClientServicesDto
import com.simple.sportly.data.remote.dto.client.BookingBulkCreateItemDto
import com.simple.sportly.data.remote.dto.client.BookingBulkItemDto
import com.simple.sportly.data.remote.dto.client.ClientBookingsDto
import com.simple.sportly.data.remote.dto.client.ClientMembershipDto
import com.simple.sportly.data.remote.dto.client.ClientProgressCreateDto
import com.simple.sportly.data.remote.dto.client.ClientProgressDto
import com.simple.sportly.data.remote.dto.client.UserTrainerPackageDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ClientServicesApi {
    @GET("api/v1/clients/me/active-services")
    suspend fun getMyActiveServices(): ActiveClientServicesDto

    @GET("api/v1/clients/me/memberships")
    suspend fun getMyMemberships(): List<ClientMembershipDto>

    @POST("api/v1/clients/me/memberships/{membership_id}/activate")
    suspend fun activateMembership(
        @Path("membership_id") membershipId: String
    ): ClientMembershipDto

    @GET("api/v1/clients/me/packages")
    suspend fun getMyPackages(): List<UserTrainerPackageDto>

    @POST("api/v1/clients/me/packages/{user_trainer_package_id}/activate")
    suspend fun activatePackage(
        @Path("user_trainer_package_id") userTrainerPackageId: String
    ): UserTrainerPackageDto

    @GET("api/v1/clients/me/progress")
    suspend fun getMyProgress(): List<ClientProgressDto>

    @GET("api/v1/clients/me/bookings")
    suspend fun getMyBookings(): ClientBookingsDto

    @POST("api/v1/clients/me/bookings/bulk")
    suspend fun createMyBulkBookings(
        @Body payload: List<BookingBulkCreateItemDto>
    ): List<BookingBulkItemDto>

    @POST("api/v1/clients/bookings/{booking_id}/cancel")
    suspend fun cancelBooking(
        @Path("booking_id") bookingId: String
    )

    @POST("api/v1/clients/me/progress")
    suspend fun createMyProgress(
        @Body request: ClientProgressCreateDto
    ): ClientProgressDto
}
