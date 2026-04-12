package com.simple.sportly.data.remote.api.client

import com.simple.sportly.data.remote.dto.client.ActiveClientServicesDto
import com.simple.sportly.data.remote.dto.client.ClientMembershipDto
import com.simple.sportly.data.remote.dto.client.UserTrainerPackageDto
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
}
