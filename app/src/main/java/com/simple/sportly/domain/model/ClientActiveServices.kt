package com.simple.sportly.domain.model

data class ClientActiveServices(
    val activeMembership: ActiveMembership?,
    val activePackage: ActivePackage?
)

data class ActiveMembership(
    val id: String,
    val membershipTypeId: String,
    val membershipTypeName: String,
    val membershipDescription: String,
    val durationMonths: Int,
    val status: String?,
    val expiresAt: String?
)

data class ActivePackage(
    val id: String,
    val trainerPackageId: String,
    val trainerId: String?,
    val trainerPackageName: String,
    val trainerPackageDescription: String?,
    val trainerPackageSessionCount: Int?,
    val trainerFirstName: String?,
    val trainerLastName: String?,
    val trainerPatronymic: String?,
    val status: String?,
    val sessionsLeft: Int?
)

data class ClientMembership(
    val id: String,
    val membershipTypeId: String,
    val membershipTypeName: String,
    val membershipDescription: String,
    val durationMonths: Int,
    val status: ClientMembershipStatus,
    val purchasedAt: String,
    val activatedAt: String?,
    val expiresAt: String?
)

data class ClientTrainerPackage(
    val id: String,
    val trainerPackageId: String,
    val trainerId: String?,
    val packageName: String,
    val packageDescription: String?,
    val sessionCount: Int,
    val trainerFirstName: String?,
    val trainerLastName: String?,
    val trainerPatronymic: String?,
    val status: ClientTrainerPackageStatus,
    val sessionsLeft: Int,
    val purchasedAt: String,
    val activatedAt: String?,
    val expiresAt: String?
)

enum class ClientMembershipStatus {
    ACTIVE,
    PURCHASED,
    EXPIRED;

    companion object {
        fun fromApi(value: String): ClientMembershipStatus {
            return entries.firstOrNull { it.name == value } ?: PURCHASED
        }
    }
}

enum class ClientTrainerPackageStatus {
    ACTIVE,
    PURCHASED,
    FINISHED;

    companion object {
        fun fromApi(value: String): ClientTrainerPackageStatus {
            return entries.firstOrNull { it.name == value } ?: PURCHASED
        }
    }
}
