package com.simple.sportly.ui.screen.client

import com.simple.sportly.domain.model.ActiveMembership
import com.simple.sportly.domain.model.ActivePackage
import com.simple.sportly.domain.model.ClientMembership
import com.simple.sportly.domain.model.ClientTrainerPackage
import com.simple.sportly.domain.model.Gym
import com.simple.sportly.domain.model.GymReview
import com.simple.sportly.domain.model.GymTrainer
import com.simple.sportly.domain.model.TrainerReview

enum class ClientTab {
    Marketplace,
    Notifications,
    Statistics,
    Profile
}

enum class GymPricesTab {
    Memberships,
    Packages
}

data class ClientHomeUiState(
    val selectedTab: ClientTab = ClientTab.Profile,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val patronymic: String = "",
    val email: String = "",
    val birthDate: String = "",
    val phone: String = "",
    val description: String = "",
    val isPushEnabled: Boolean = false,
    val marketplaceSearchQuery: String = "",
    val marketplaceCityFilter: String = "",
    val marketplaceRatingFilter: Int? = null,
    val gyms: List<Gym> = emptyList(),
    val selectedGym: Gym? = null,
    val isGymPricesOpened: Boolean = false,
    val isGymReviewsOpened: Boolean = false,
    val isGymTrainersOpened: Boolean = false,
    val isStatisticsMembershipsOpened: Boolean = false,
    val isStatisticsPackagesOpened: Boolean = false,
    val activeMembership: ActiveMembership? = null,
    val activePackage: ActivePackage? = null,
    val isActiveServicesLoading: Boolean = false,
    val activeServicesErrorMessage: String? = null,
    val memberships: List<ClientMembership> = emptyList(),
    val isMembershipsLoading: Boolean = false,
    val membershipsErrorMessage: String? = null,
    val activatingMembershipId: String? = null,
    val packages: List<ClientTrainerPackage> = emptyList(),
    val isPackagesLoading: Boolean = false,
    val packagesErrorMessage: String? = null,
    val activatingPackageId: String? = null,
    val gymPricesTab: GymPricesTab = GymPricesTab.Memberships,
    val gymReviews: List<GymReview> = emptyList(),
    val gymReviewsForGymId: String? = null,
    val gymReviewsAverageRating: Double? = null,
    val isGymReviewsLoading: Boolean = false,
    val gymReviewsErrorMessage: String? = null,
    val gymTrainers: List<GymTrainer> = emptyList(),
    val gymTrainersForGymId: String? = null,
    val selectedTrainer: GymTrainer? = null,
    val isTrainerReviewsOpened: Boolean = false,
    val trainerReviews: List<TrainerReview> = emptyList(),
    val trainerReviewsForTrainerId: String? = null,
    val trainerReviewsAverageRating: Double? = null,
    val isTrainerReviewsLoading: Boolean = false,
    val trainerReviewsErrorMessage: String? = null,
    val isGymTrainersLoading: Boolean = false,
    val gymTrainersErrorMessage: String? = null,
    val isGymsLoading: Boolean = false,
    val gymsErrorMessage: String? = null,
    val errorMessage: String? = null,
    val infoMessage: String? = null
)

