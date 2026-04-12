package com.simple.sportly.ui.screen.client

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.draw.clip
import coil.compose.SubcomposeAsyncImage
import com.simple.sportly.BuildConfig
import com.simple.sportly.R
import com.simple.sportly.domain.model.ActiveMembership
import com.simple.sportly.domain.model.ActivePackage
import com.simple.sportly.domain.model.ClientMembership
import com.simple.sportly.domain.model.ClientMembershipStatus
import com.simple.sportly.domain.model.ClientTrainerPackage
import com.simple.sportly.domain.model.ClientTrainerPackageStatus
import com.simple.sportly.domain.model.Gym
import com.simple.sportly.domain.model.GymMembershipType
import com.simple.sportly.domain.model.GymReview
import com.simple.sportly.domain.model.GymTrainer
import com.simple.sportly.domain.model.GymTrainerPackage
import com.simple.sportly.domain.model.TrainerReview
import com.simple.sportly.domain.model.TrainerSlot
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

internal val ScreenBg = Color(0xFFE7E3DA)
internal val CardBg = Color(0xFFD1CAB9)
internal val AccentDark = Color(0xFF565347)
internal val MainText = Color(0xFF29251F)

@Composable
fun ClientHomeScreen(
    state: ClientHomeUiState,
    onTabSelected: (ClientTab) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPatronymicChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPushEnabledChange: (Boolean) -> Unit,
    onMarketplaceSearchQueryChange: (String) -> Unit,
    onMarketplaceCityFilterChange: (String) -> Unit,
    onMarketplaceRatingFilterChange: (Int?) -> Unit,
    onMarketplaceSearchClick: () -> Unit,
    onMarketplaceGymClick: (Gym) -> Unit,
    onMarketplaceBackToListClick: () -> Unit,
    onMarketplacePricesClick: () -> Unit,
    onMarketplaceBackFromPricesClick: () -> Unit,
    onMarketplacePricesTabSelected: (GymPricesTab) -> Unit,
    onMarketplaceReviewsClick: () -> Unit,
    onMarketplaceBackFromReviewsClick: () -> Unit,
    onMarketplaceTrainersClick: () -> Unit,
    onMarketplaceBackFromTrainersClick: () -> Unit,
    onMarketplaceTrainerClick: (GymTrainer) -> Unit,
    onMarketplaceBackFromTrainerDetailsClick: () -> Unit,
    onMarketplaceTrainerReviewsClick: () -> Unit,
    onMarketplaceBackFromTrainerReviewsClick: () -> Unit,
    onRefreshActiveServices: () -> Unit,
    onOpenStatisticsMemberships: () -> Unit,
    onCloseStatisticsMemberships: () -> Unit,
    onRefreshMemberships: () -> Unit,
    onActivateMembership: (String) -> Unit,
    onOpenStatisticsPackages: () -> Unit,
    onCloseStatisticsPackages: () -> Unit,
    onRefreshPackages: () -> Unit,
    onActivatePackage: (String) -> Unit,
    onOpenStatisticsPackageBooking: (String) -> Unit,
    onCloseStatisticsPackageBooking: () -> Unit,
    onBookingDateSelected: (LocalDate) -> Unit,
    onToggleBookingSlot: (TrainerSlot) -> Unit,
    onRemoveSelectedBookingSlot: (String) -> Unit,
    onRetryBookingSlots: () -> Unit,
    onSubmitBulkBooking: () -> Unit,
    onOpenStatisticsWeight: () -> Unit,
    onCloseStatisticsWeight: () -> Unit,
    onOpenStatisticsTrainings: () -> Unit,
    onCloseStatisticsTrainings: () -> Unit,
    onSelectMyTrainingsTab: (MyTrainingsTab) -> Unit,
    onRefreshBookings: () -> Unit,
    onCancelBooking: (String) -> Unit,
    onLeaveBookingReview: (String) -> Unit,
    onCloseTrainerReviewForm: () -> Unit,
    onTrainerReviewRatingChange: (Int) -> Unit,
    onTrainerReviewCommentChange: (String) -> Unit,
    onShouldLeaveGymReviewChange: (Boolean) -> Unit,
    onSubmitTrainerReview: () -> Unit,
    onCloseGymReviewForm: () -> Unit,
    onGymReviewRatingChange: (Int) -> Unit,
    onGymReviewCommentChange: (String) -> Unit,
    onSubmitGymReview: () -> Unit,
    onWeightInputChange: (String) -> Unit,
    onHeightInputChange: (String) -> Unit,
    onCalculateBmiClick: () -> Unit,
    onOpenWeightDynamics: () -> Unit,
    onCloseWeightDynamics: () -> Unit,
    onRefreshWeightDynamics: () -> Unit,
    onSaveClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Scaffold(
        containerColor = ScreenBg,
        bottomBar = {
            ClientBottomBar(
                selectedTab = state.selectedTab,
                onTabSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ScreenBg)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                return@Box
            }

            when (state.selectedTab) {
                ClientTab.Profile -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 22.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        RoundedInput(
                            value = state.lastName,
                            onValueChange = onLastNameChange,
                            placeholder = "Фамилия"
                        )
                        RoundedInput(
                            value = state.firstName,
                            onValueChange = onFirstNameChange,
                            placeholder = "Имя"
                        )
                        RoundedInput(
                            value = state.patronymic,
                            onValueChange = onPatronymicChange,
                            placeholder = "Отчество"
                        )
                        RoundedInput(
                            value = state.email,
                            onValueChange = onEmailChange,
                            placeholder = "Email",
                            keyboardType = KeyboardType.Email
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp, bottom = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Push-уведомления",
                                color = MainText,
                                fontFamily = FontFamily.Serif,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Switch(
                                checked = state.isPushEnabled,
                                onCheckedChange = onPushEnabledChange,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = ScreenBg,
                                    checkedTrackColor = AccentDark,
                                    uncheckedThumbColor = ScreenBg,
                                    uncheckedTrackColor = CardBg.copy(alpha = 0.7f),
                                    uncheckedBorderColor = AccentDark.copy(alpha = 0.6f)
                                )
                            )
                        }

                        if (!state.errorMessage.isNullOrBlank()) {
                            Text(
                                text = state.errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        if (!state.infoMessage.isNullOrBlank()) {
                            Text(
                                text = state.infoMessage,
                                color = AccentDark,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        Button(
                            onClick = onSaveClick,
                            enabled = !state.isSaving,
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentDark,
                                contentColor = ScreenBg
                            ),
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .size(width = 200.dp, height = 52.dp)
                        ) {
                            if (state.isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = ScreenBg,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "Сохранить",
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    IconButton(
                        onClick = onLogoutClick,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 8.dp, end = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = MainText
                        )
                    }
                }

                ClientTab.Marketplace -> MarketplaceTab(
                    state = state,
                    onSearchQueryChange = onMarketplaceSearchQueryChange,
                    onCityFilterChange = onMarketplaceCityFilterChange,
                    onRatingFilterChange = onMarketplaceRatingFilterChange,
                    onSearchClick = onMarketplaceSearchClick,
                    onGymClick = onMarketplaceGymClick,
                    onBackToListClick = onMarketplaceBackToListClick,
                    onPricesClick = onMarketplacePricesClick,
                    onBackFromPricesClick = onMarketplaceBackFromPricesClick,
                    onPricesTabSelected = onMarketplacePricesTabSelected,
                    onReviewsClick = onMarketplaceReviewsClick,
                    onBackFromReviewsClick = onMarketplaceBackFromReviewsClick,
                    onTrainersClick = onMarketplaceTrainersClick,
                    onBackFromTrainersClick = onMarketplaceBackFromTrainersClick,
                    onTrainerClick = onMarketplaceTrainerClick,
                    onBackFromTrainerDetailsClick = onMarketplaceBackFromTrainerDetailsClick,
                    onTrainerReviewsClick = onMarketplaceTrainerReviewsClick,
                    onBackFromTrainerReviewsClick = onMarketplaceBackFromTrainerReviewsClick
                )
                ClientTab.Notifications -> PlaceholderTab(text = "Уведомления")
                ClientTab.Statistics -> {
                    if (state.isStatisticsMembershipsOpened) {
                        BackHandler(onBack = onCloseStatisticsMemberships)
                        MembershipsStatisticsPage(
                            memberships = state.memberships,
                            isLoading = state.isMembershipsLoading,
                            errorMessage = state.membershipsErrorMessage,
                            activatingMembershipId = state.activatingMembershipId,
                            onBackClick = onCloseStatisticsMemberships,
                            onRetryClick = onRefreshMemberships,
                            onActivateClick = onActivateMembership
                        )
                    } else if (state.isStatisticsPackagesOpened) {
                        BackHandler(onBack = onCloseStatisticsPackages)
                        PackagesStatisticsPage(
                            packages = state.packages,
                            isLoading = state.isPackagesLoading,
                            errorMessage = state.packagesErrorMessage,
                            activatingPackageId = state.activatingPackageId,
                            onBackClick = onCloseStatisticsPackages,
                            onRetryClick = onRefreshPackages,
                            onActivateClick = onActivatePackage,
                            onBookClick = { onOpenStatisticsPackageBooking(it.id) }
                        )
                    } else if (state.isStatisticsPackageBookingOpened) {
                        BackHandler(onBack = onCloseStatisticsPackageBooking)
                        PackageBookingStatisticsPage(
                            packageItem = state.bookingPackage,
                            selectedDate = state.bookingSelectedDate,
                            availableSlots = state.bookingAvailableSlots,
                            selectedSlots = state.selectedBookingSlots,
                            isSlotsLoading = state.isBookingSlotsLoading,
                            isSubmitting = state.isBookingSubmitting,
                            errorMessage = state.bookingSlotsErrorMessage,
                            infoMessage = state.bookingInfoMessage,
                            onBackClick = onCloseStatisticsPackageBooking,
                            onDateSelected = onBookingDateSelected,
                            onSlotClick = onToggleBookingSlot,
                            onRemoveSelectedClick = onRemoveSelectedBookingSlot,
                            onRetrySlotsClick = onRetryBookingSlots,
                            onSubmitClick = onSubmitBulkBooking
                        )
                    } else if (state.isStatisticsWeightOpened && state.isStatisticsWeightDynamicsOpened) {
                        BackHandler(onBack = onCloseWeightDynamics)
                        WeightDynamicsPage(
                            progressHistory = state.progressHistory,
                            isLoading = state.isProgressLoading,
                            errorMessage = state.progressLoadErrorMessage,
                            onBackClick = onCloseWeightDynamics,
                            onRetryClick = onRefreshWeightDynamics
                        )
                    } else if (state.isStatisticsWeightOpened) {
                        BackHandler(onBack = onCloseStatisticsWeight)
                        WeightStatisticsPage(
                            weightValue = state.weightInput,
                            heightValue = state.heightInput,
                            bmiValue = state.bmiValue,
                            isSaving = state.isProgressSaving,
                            errorMessage = state.progressSaveErrorMessage,
                            infoMessage = state.progressSaveInfoMessage,
                            onBackClick = onCloseStatisticsWeight,
                            onWeightChange = onWeightInputChange,
                            onHeightChange = onHeightInputChange,
                            onCalculateClick = onCalculateBmiClick,
                            onWeightDynamicsClick = onOpenWeightDynamics
                        )
                    } else if (state.isTrainerReviewFormOpened) {
                        BackHandler(onBack = onCloseTrainerReviewForm)
                        TrainerReviewFormPage(
                            booking = state.reviewBooking,
                            rating = state.trainerReviewRating,
                            comment = state.trainerReviewComment,
                            leaveGymReview = state.shouldLeaveGymReview,
                            isSubmitting = state.isTrainerReviewSubmitting,
                            errorMessage = state.reviewErrorMessage,
                            onCloseClick = onCloseTrainerReviewForm,
                            onRatingChange = onTrainerReviewRatingChange,
                            onCommentChange = onTrainerReviewCommentChange,
                            onLeaveGymReviewChange = onShouldLeaveGymReviewChange,
                            onSubmitClick = onSubmitTrainerReview
                        )
                    } else if (state.isGymReviewFormOpened) {
                        BackHandler(onBack = onCloseGymReviewForm)
                        GymReviewFormPage(
                            booking = state.reviewBooking,
                            rating = state.gymReviewRating,
                            comment = state.gymReviewComment,
                            isSubmitting = state.isGymReviewSubmitting,
                            errorMessage = state.reviewErrorMessage,
                            onCloseClick = onCloseGymReviewForm,
                            onRatingChange = onGymReviewRatingChange,
                            onCommentChange = onGymReviewCommentChange,
                            onSubmitClick = onSubmitGymReview
                        )
                    } else if (state.isStatisticsTrainingsOpened) {
                        BackHandler(onBack = onCloseStatisticsTrainings)
                        MyTrainingsStatisticsPage(
                            selectedTab = state.selectedMyTrainingsTab,
                            upcomingBookings = state.upcomingBookings,
                            pastBookings = state.pastBookings,
                            isLoading = state.isBookingsLoading,
                            errorMessage = state.bookingsErrorMessage,
                            cancellingBookingId = state.cancellingBookingId,
                            onBackClick = onCloseStatisticsTrainings,
                            onRetryClick = onRefreshBookings,
                            onTabSelected = onSelectMyTrainingsTab,
                            onCancelClick = { onCancelBooking(it.id) },
                            onLeaveReviewClick = { onLeaveBookingReview(it.id) }
                        )
                    } else {
                        StatisticsTab(
                            activeMembership = state.activeMembership,
                            activePackage = state.activePackage,
                            isLoading = state.isActiveServicesLoading,
                            errorMessage = state.activeServicesErrorMessage,
                            onRetryClick = onRefreshActiveServices,
                            onMembershipsClick = onOpenStatisticsMemberships,
                            onPackagesClick = onOpenStatisticsPackages,
                            onWeightClick = onOpenStatisticsWeight,
                            onMyTrainingsClick = onOpenStatisticsTrainings
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlaceholderTab(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text, color = MainText, fontFamily = FontFamily.Serif)
    }
}

@Composable
private fun RoundedInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
                color = MainText.copy(alpha = 0.55f),
                fontFamily = FontFamily.Serif
            )
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MainText,
            fontFamily = FontFamily.Serif
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = CardBg,
            unfocusedContainerColor = CardBg,
            disabledContainerColor = CardBg,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = MainText
        ),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
    )
}

@Composable
private fun ClientBottomBar(
    selectedTab: ClientTab,
    onTabSelected: (ClientTab) -> Unit
) {
    Surface(
        color = CardBg,
        shape = RoundedCornerShape(34.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 10.dp)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarItem(
                isSelected = selectedTab == ClientTab.Marketplace,
                icon = {
                    Icon(
                        imageVector = ClientBottomIcons.Activities,
                        contentDescription = "Activities"
                    )
                },
                onClick = { onTabSelected(ClientTab.Marketplace) }
            )
            BottomBarItem(
                isSelected = selectedTab == ClientTab.Notifications,
                icon = {
                    Icon(
                        imageVector = ClientBottomIcons.Notifications,
                        contentDescription = "Notifications"
                    )
                },
                onClick = { onTabSelected(ClientTab.Notifications) }
            )
            BottomBarItem(
                isSelected = selectedTab == ClientTab.Statistics,
                icon = {
                    Icon(
                        imageVector = ClientBottomIcons.Refresh,
                        contentDescription = "Refresh"
                    )
                },
                onClick = { onTabSelected(ClientTab.Statistics) }
            )
            BottomBarItem(
                isSelected = selectedTab == ClientTab.Profile,
                icon = {
                    Icon(
                        imageVector = ClientBottomIcons.Profile,
                        contentDescription = "Profile"
                    )
                },
                onClick = { onTabSelected(ClientTab.Profile) }
            )
        }
    }
}

@Composable
private fun BottomBarItem(
    isSelected: Boolean,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) ScreenBg else MainText
    val itemModifier = if (isSelected) {
        Modifier
            .size(52.dp)
            .background(AccentDark, CircleShape)
    } else {
        Modifier.size(52.dp)
    }

    Box(
        modifier = itemModifier.clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            icon()
        }
    }
}
