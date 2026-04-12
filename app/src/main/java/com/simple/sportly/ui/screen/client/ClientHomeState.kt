package com.simple.sportly.ui.screen.client

import com.simple.sportly.domain.model.ActiveMembership
import com.simple.sportly.domain.model.ActivePackage
import com.simple.sportly.domain.model.ClientBooking
import com.simple.sportly.domain.model.ClientMembership
import com.simple.sportly.domain.model.ClientProgress
import com.simple.sportly.domain.model.ClientTrainerPackage
import com.simple.sportly.domain.model.Gym
import com.simple.sportly.domain.model.GymReview
import com.simple.sportly.domain.model.GymTrainer
import com.simple.sportly.domain.model.TrainerReview
import com.simple.sportly.domain.model.TrainerSlot
import java.time.LocalDate

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

enum class MyTrainingsTab {
    Upcoming,
    Past
}

data class PackageBookingSelection(
    val slotId: String,
    val date: LocalDate,
    val startTime: String,
    val endTime: String
)

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
    val isStatisticsPackageBookingOpened: Boolean = false,
    val isStatisticsWeightOpened: Boolean = false,
    val isStatisticsWeightDynamicsOpened: Boolean = false,
    val isStatisticsTrainingsOpened: Boolean = false,
    val isTrainerReviewFormOpened: Boolean = false,
    val isGymReviewFormOpened: Boolean = false,
    val selectedMyTrainingsTab: MyTrainingsTab = MyTrainingsTab.Upcoming,
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
    val bookingPackage: ClientTrainerPackage? = null,
    val bookingSelectedDate: LocalDate = LocalDate.now(),
    val bookingAvailableSlots: List<TrainerSlot> = emptyList(),
    val selectedBookingSlots: List<PackageBookingSelection> = emptyList(),
    val isBookingSlotsLoading: Boolean = false,
    val isBookingSubmitting: Boolean = false,
    val bookingSlotsErrorMessage: String? = null,
    val bookingInfoMessage: String? = null,
    val weightInput: String = "",
    val heightInput: String = "",
    val bmiValue: Double? = null,
    val isProgressSaving: Boolean = false,
    val progressSaveErrorMessage: String? = null,
    val progressSaveInfoMessage: String? = null,
    val progressHistory: List<ClientProgress> = emptyList(),
    val isProgressLoading: Boolean = false,
    val progressLoadErrorMessage: String? = null,
    val upcomingBookings: List<ClientBooking> = emptyList(),
    val pastBookings: List<ClientBooking> = emptyList(),
    val isBookingsLoading: Boolean = false,
    val bookingsErrorMessage: String? = null,
    val cancellingBookingId: String? = null,
    val reviewBooking: ClientBooking? = null,
    val trainerReviewRating: Int = 0,
    val trainerReviewComment: String = "",
    val shouldLeaveGymReview: Boolean = false,
    val gymReviewRating: Int = 0,
    val gymReviewComment: String = "",
    val isTrainerReviewSubmitting: Boolean = false,
    val isGymReviewSubmitting: Boolean = false,
    val reviewErrorMessage: String? = null,
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

