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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import coil.compose.SubcomposeAsyncImage
import com.simple.sportly.BuildConfig
import com.simple.sportly.R
import com.simple.sportly.domain.model.Gym
import com.simple.sportly.domain.model.GymMembershipType
import com.simple.sportly.domain.model.GymReview
import com.simple.sportly.domain.model.GymTrainer
import com.simple.sportly.domain.model.GymTrainerPackage
import com.simple.sportly.domain.model.TrainerReview

private val ScreenBg = Color(0xFFE7E3DA)
private val CardBg = Color(0xFFD1CAB9)
private val AccentDark = Color(0xFF565347)
private val MainText = Color(0xFF29251F)

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

                ClientTab.Activities -> MarketplaceTab(
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
                ClientTab.Refresh -> PlaceholderTab(text = "Статистика")
            }
        }
    }
}

@Composable
private fun MarketplaceTab(
    state: ClientHomeUiState,
    onSearchQueryChange: (String) -> Unit,
    onCityFilterChange: (String) -> Unit,
    onRatingFilterChange: (Int?) -> Unit,
    onSearchClick: () -> Unit,
    onGymClick: (Gym) -> Unit,
    onBackToListClick: () -> Unit,
    onPricesClick: () -> Unit,
    onBackFromPricesClick: () -> Unit,
    onPricesTabSelected: (GymPricesTab) -> Unit,
    onReviewsClick: () -> Unit,
    onBackFromReviewsClick: () -> Unit,
    onTrainersClick: () -> Unit,
    onBackFromTrainersClick: () -> Unit,
    onTrainerClick: (GymTrainer) -> Unit,
    onBackFromTrainerDetailsClick: () -> Unit,
    onTrainerReviewsClick: () -> Unit,
    onBackFromTrainerReviewsClick: () -> Unit
) {
    var isFilterDialogOpen by rememberSaveable { mutableStateOf(false) }

    state.selectedGym?.let { gym ->
        if (state.isGymTrainersOpened) {
            val selectedTrainer = state.selectedTrainer
            if (selectedTrainer != null && state.isTrainerReviewsOpened) {
                BackHandler(onBack = onBackFromTrainerReviewsClick)
                TrainerReviewsPage(
                    averageRating = state.trainerReviewsAverageRating,
                    reviews = state.trainerReviews,
                    isLoading = state.isTrainerReviewsLoading,
                    errorMessage = state.trainerReviewsErrorMessage,
                    onBackClick = onBackFromTrainerReviewsClick,
                    onRetryClick = onTrainerReviewsClick
                )
            } else if (selectedTrainer != null) {
                BackHandler(onBack = onBackFromTrainerDetailsClick)
                TrainerDetailsPage(
                    trainer = selectedTrainer,
                    onBackClick = onBackFromTrainerDetailsClick,
                    onReviewsClick = onTrainerReviewsClick
                )
            } else {
                BackHandler(onBack = onBackFromTrainersClick)
                GymTrainersPage(
                    trainers = state.gymTrainers,
                    isLoading = state.isGymTrainersLoading,
                    errorMessage = state.gymTrainersErrorMessage,
                    onBackClick = onBackFromTrainersClick,
                    onRetryClick = onTrainersClick,
                    onTrainerClick = onTrainerClick
                )
            }
        } else if (state.isGymReviewsOpened) {
            BackHandler(onBack = onBackFromReviewsClick)
            GymReviewsPage(
                averageRating = state.gymReviewsAverageRating,
                reviews = state.gymReviews,
                isLoading = state.isGymReviewsLoading,
                errorMessage = state.gymReviewsErrorMessage,
                onBackClick = onBackFromReviewsClick,
                onRetryClick = onReviewsClick
            )
        } else if (state.isGymPricesOpened) {
            BackHandler(onBack = onBackFromPricesClick)
            GymPricesPage(
                gym = gym,
                selectedTab = state.gymPricesTab,
                onBackClick = onBackFromPricesClick,
                onTabSelected = onPricesTabSelected
            )
        } else {
            BackHandler(onBack = onBackToListClick)
            GymDetailsPage(
                gym = gym,
                onBackClick = onBackToListClick,
                onPricesClick = onPricesClick,
                onReviewsClick = onReviewsClick,
                onTrainersClick = onTrainersClick
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = state.marketplaceSearchQuery,
                onValueChange = onSearchQueryChange,
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Поиск зала",
                        color = MainText.copy(alpha = 0.55f),
                        fontFamily = FontFamily.Serif
                    )
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MainText,
                    fontFamily = FontFamily.Serif
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = CardBg,
                    unfocusedContainerColor = CardBg,
                    disabledContainerColor = CardBg,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MainText
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { isFilterDialogOpen = true }) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter gyms",
                    tint = MainText
                )
            }
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search gyms",
                    tint = MainText
                )
            }
        }

        when {
            state.isGymsLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AccentDark)
                }
            }

            !state.gymsErrorMessage.isNullOrBlank() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.gymsErrorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onSearchClick) {
                        Text("Retry")
                    }
                }
            }

            state.gyms.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Gyms not found",
                        color = MainText,
                        fontFamily = FontFamily.Serif
                    )
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp)
                ) {
                    items(state.gyms, key = { it.id }) { gym ->
                        GymCard(
                            gym = gym,
                            onClick = { onGymClick(gym) }
                        )
                    }
                }
            }
        }
    }

    if (isFilterDialogOpen) {
        MarketplaceFilterDialog(
            city = state.marketplaceCityFilter,
            selectedRating = state.marketplaceRatingFilter,
            onCityChange = onCityFilterChange,
            onRatingChange = onRatingFilterChange,
            onDismiss = { isFilterDialogOpen = false },
            onApply = {
                isFilterDialogOpen = false
                onSearchClick()
            },
            onReset = {
                onCityFilterChange("")
                onRatingFilterChange(null)
            }
        )
    }
}

@Composable
private fun MarketplaceFilterDialog(
    city: String,
    selectedRating: Int?,
    onCityChange: (String) -> Unit,
    onRatingChange: (Int?) -> Unit,
    onDismiss: () -> Unit,
    onApply: () -> Unit,
    onReset: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ScreenBg,
        title = {
            Text(
                text = "Фильтры",
                color = MainText,
                fontFamily = FontFamily.Serif
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = city,
                    onValueChange = onCityChange,
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Введите город",
                            color = MainText.copy(alpha = 0.55f),
                            fontFamily = FontFamily.Serif
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MainText,
                        fontFamily = FontFamily.Serif
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MainText,
                        unfocusedTextColor = MainText,
                        focusedContainerColor = ScreenBg,
                        unfocusedContainerColor = ScreenBg,
                        focusedBorderColor = AccentDark,
                        unfocusedBorderColor = AccentDark.copy(alpha = 0.7f),
                        cursorColor = AccentDark
                    )
                )
                Text(
                    text = "Рейтинг",
                    color = MainText,
                    fontFamily = FontFamily.Serif
                )
                BoxWithConstraints {
                    val widthPerChip = (maxWidth - 16.dp) / 3
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            RatingChip(
                                label = "Любой",
                                selected = selectedRating == null,
                                onClick = { onRatingChange(null) },
                                width = widthPerChip
                            )
                            RatingChip(
                                label = "5",
                                selected = selectedRating == 5,
                                onClick = { onRatingChange(5) },
                                width = widthPerChip
                            )
                            RatingChip(
                                label = "4",
                                selected = selectedRating == 4,
                                onClick = { onRatingChange(4) },
                                width = widthPerChip
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            RatingChip(
                                label = "3",
                                selected = selectedRating == 3,
                                onClick = { onRatingChange(3) },
                                width = widthPerChip
                            )
                            RatingChip(
                                label = "2",
                                selected = selectedRating == 2,
                                onClick = { onRatingChange(2) },
                                width = widthPerChip
                            )
                            RatingChip(
                                label = "1",
                                selected = selectedRating == 1,
                                onClick = { onRatingChange(1) },
                                width = widthPerChip
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onApply,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentDark,
                    contentColor = ScreenBg
                )
            ) {
                Text(
                    text = "Применить",
                    fontFamily = FontFamily.Serif
                )
            }
        },
        dismissButton = {
            Row {
                TextButton(
                    onClick = onReset,
                    colors = ButtonDefaults.textButtonColors(contentColor = AccentDark)
                ) {
                    Text(
                        text = "Сбросить",
                        fontFamily = FontFamily.Serif
                    )
                }
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(contentColor = AccentDark)
                ) {
                    Text(
                        text = "Закрыть",
                        fontFamily = FontFamily.Serif
                    )
                }
            }
        }
    )
}

@Composable
private fun RatingChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    width: androidx.compose.ui.unit.Dp
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                fontFamily = FontFamily.Serif
            )
        },
        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
            containerColor = ScreenBg,
            labelColor = MainText,
            selectedContainerColor = AccentDark,
            selectedLabelColor = ScreenBg
        ),
        modifier = Modifier.width(width)
    )
}

@Composable
private fun GymCard(
    gym: Gym,
    onClick: () -> Unit
) {
    val gymPhotoUrl = "${BuildConfig.BASE_URL.trimEnd('/')}/gyms/${gym.id}.jpg"
    val ratingText = gym.rating?.let { String.format("%.1f", it).replace('.', ',') } ?: "нет"

    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            SubcomposeAsyncImage(
                model = gymPhotoUrl,
                contentDescription = "Gym logo",
                loading = { GymImagePlaceholder() },
                error = { GymImagePlaceholder() },
                modifier = Modifier
                    .size(98.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = gym.title.uppercase(),
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = gym.address,
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Рейтинг: $ratingText",
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun GymDetailsPage(
    gym: Gym,
    onBackClick: () -> Unit,
    onPricesClick: () -> Unit,
    onReviewsClick: () -> Unit,
    onTrainersClick: () -> Unit
) {
    val ratingText = gym.rating?.let { String.format("%.1f", it).replace('.', ',') } ?: "нет"
    val phoneText = gym.phone?.ifBlank { null } ?: "не указан"
    val descriptionText = gym.description?.ifBlank { null } ?: "не указано"
    val scheduleLines = remember(gym.schedule) { gym.formatSchedule() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Назад",
                    tint = MainText
                )
            }
            SubcomposeAsyncImage(
                model = "${BuildConfig.BASE_URL.trimEnd('/')}/gyms/${gym.id}.jpg",
                contentDescription = "Логотип зала",
                loading = { GymImagePlaceholder() },
                error = { GymImagePlaceholder() },
                modifier = Modifier
                    .size(130.dp)
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(4.dp))
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Название: ${gym.title}",
            color = MainText,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Адрес: ${gym.address}",
            color = MainText,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Рейтинг: $ratingText",
            color = MainText,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Номер телефона: $phoneText",
            color = MainText,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Описание: $descriptionText",
            color = MainText,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Расписание работы:",
            color = MainText,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        scheduleLines.forEach { line ->
            Text(
                text = line,
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        GymActionButton(
            text = "Цены",
            onClick = onPricesClick
        )
        Spacer(modifier = Modifier.height(10.dp))
        GymActionButton(
            text = "Тренеры",
            onClick = onTrainersClick
        )
        Spacer(modifier = Modifier.height(10.dp))
        GymActionButton(
            text = "Отзывы",
            onClick = onReviewsClick
        )
        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Composable
private fun GymActionButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentDark,
            contentColor = ScreenBg
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
            .height(46.dp)
    ) {
        Text(
            text = text,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun GymPricesPage(
    gym: Gym,
    selectedTab: GymPricesTab,
    onBackClick: () -> Unit,
    onTabSelected: (GymPricesTab) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Назад",
                    tint = MainText
                )
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(CardBg, RoundedCornerShape(6.dp))
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PricesTabButton(
                    text = "Членства",
                    selected = selectedTab == GymPricesTab.Memberships,
                    onClick = { onTabSelected(GymPricesTab.Memberships) },
                    modifier = Modifier.weight(1f)
                )
                PricesTabButton(
                    text = "Пакеты",
                    selected = selectedTab == GymPricesTab.Packages,
                    onClick = { onTabSelected(GymPricesTab.Packages) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        when (selectedTab) {
            GymPricesTab.Memberships -> MembershipsList(items = gym.membershipTypes)
            GymPricesTab.Packages -> TrainerPackagesList(items = gym.trainerPackages)
        }
    }
}

@Composable
private fun GymReviewsPage(
    averageRating: Double?,
    reviews: List<GymReview>,
    isLoading: Boolean,
    errorMessage: String?,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    val averageText = averageRating?.let { String.format("%.1f", it).replace('.', ',') } ?: "нет"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Назад",
                    tint = MainText
                )
            }
            Text(
                text = "Рейтинг: $averageText",
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AccentDark)
                }
            }

            !errorMessage.isNullOrBlank() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onRetryClick) {
                        Text("Повторить")
                    }
                }
            }

            reviews.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Отзывов пока нет",
                        color = MainText,
                        fontFamily = FontFamily.Serif
                    )
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(reviews, key = { it.id }) { review ->
                        GymReviewCard(review = review)
                    }
                }
            }
        }
    }
}

@Composable
private fun GymTrainersPage(
    trainers: List<GymTrainer>,
    isLoading: Boolean,
    errorMessage: String?,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    onTrainerClick: (GymTrainer) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Назад",
                    tint = MainText
                )
            }
        }

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AccentDark)
                }
            }

            !errorMessage.isNullOrBlank() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onRetryClick) {
                        Text("Повторить")
                    }
                }
            }

            trainers.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Тренеры не найдены",
                        color = MainText,
                        fontFamily = FontFamily.Serif
                    )
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(trainers, key = { it.id }) { trainer ->
                        GymTrainerCard(
                            trainer = trainer,
                            onClick = { onTrainerClick(trainer) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GymTrainerCard(
    trainer: GymTrainer,
    onClick: () -> Unit
) {
    val ratingText = trainer.rating?.let { String.format("%.1f", it).replace('.', ',') } ?: "нет"
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            SubcomposeAsyncImage(
                model = "${BuildConfig.BASE_URL.trimEnd('/')}/avatars/${trainer.userId}.jpg",
                contentDescription = "Фото тренера",
                loading = { TrainerAvatarPlaceholder() },
                error = { TrainerAvatarPlaceholder() },
                modifier = Modifier
                    .size(width = 98.dp, height = 130.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = trainer.fullName,
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = trainer.description,
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Рейтинг: $ratingText",
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun TrainerAvatarPlaceholder() {
    Box(
        modifier = Modifier
            .size(width = 98.dp, height = 130.dp)
            .background(ScreenBg, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Image(
            painter = painterResource(id = R.drawable.ic_trainer_placeholder),
            contentDescription = null,
            modifier = Modifier.size(56.dp)
        )
    }
}

@Composable
private fun GymReviewCard(review: GymReview) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.authorFullName,
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = review.rating.toStars(),
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            if (!review.comment.isNullOrBlank()) {
                Text(
                    text = review.comment,
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun TrainerDetailsPage(
    trainer: GymTrainer,
    onBackClick: () -> Unit,
    onReviewsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Назад",
                    tint = MainText
                )
            }
            SubcomposeAsyncImage(
                model = "${BuildConfig.BASE_URL.trimEnd('/')}/avatars/${trainer.userId}.jpg",
                contentDescription = "Фото тренера",
                loading = { TrainerAvatarPlaceholder() },
                error = { TrainerAvatarPlaceholder() },
                modifier = Modifier
                    .size(width = 120.dp, height = 170.dp)
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(4.dp))
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = trainer.fullName,
            color = MainText,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = trainer.description,
            color = MainText,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(18.dp))
        GymActionButton(
            text = "Отзывы",
            onClick = onReviewsClick
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun TrainerReviewsPage(
    averageRating: Double?,
    reviews: List<TrainerReview>,
    isLoading: Boolean,
    errorMessage: String?,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    val averageText = averageRating?.let { String.format("%.1f", it).replace('.', ',') } ?: "нет"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Назад",
                    tint = MainText
                )
            }
            Text(
                text = "Рейтинг: $averageText",
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AccentDark)
                }
            }

            !errorMessage.isNullOrBlank() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onRetryClick) {
                        Text("Повторить")
                    }
                }
            }

            reviews.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Отзывов пока нет",
                        color = MainText,
                        fontFamily = FontFamily.Serif
                    )
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(reviews, key = { it.id }) { review ->
                        TrainerReviewCard(review = review)
                    }
                }
            }
        }
    }
}

@Composable
private fun TrainerReviewCard(review: TrainerReview) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.authorFullName,
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = review.rating.toStars(),
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            if (!review.comment.isNullOrBlank()) {
                Text(
                    text = review.comment,
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun PricesTabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (selected) AccentDark else Color.Transparent
    val backgroundColor = if (selected) ScreenBg else CardBg
    Button(
        onClick = onClick,
        modifier = modifier.height(34.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = MainText
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
    ) {
        Text(
            text = text,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun MembershipsList(items: List<GymMembershipType>) {
    if (items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Услуги не найдены", color = MainText, fontFamily = FontFamily.Serif)
        }
        return
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items, key = { it.id }) { membership ->
            ServiceCard(
                title = membership.name,
                description = membership.description,
                priceText = membership.price.toRubPrice()
            )
        }
    }
}

@Composable
private fun TrainerPackagesList(items: List<GymTrainerPackage>) {
    if (items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Услуги не найдены", color = MainText, fontFamily = FontFamily.Serif)
        }
        return
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items, key = { it.id }) { trainerPackage ->
            val description = trainerPackage.description
                ?.takeIf { it.isNotBlank() }
                ?: "Пакет на ${trainerPackage.sessionCount} занятий."
            ServiceCard(
                title = trainerPackage.name,
                description = description,
                priceText = trainerPackage.price.toRubPrice()
            )
        }
    }
}

@Composable
private fun ServiceCard(
    title: String,
    description: String,
    priceText: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = description,
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = priceText,
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

private fun String.toRubPrice(): String {
    val normalized = trim().replace(',', '.')
    val number = normalized.toDoubleOrNull()
    if (number == null) return "$this р."

    val intPart = number.toLong()
    val grouped = intPart.toString().reversed().chunked(3).joinToString(" ").reversed()
    return "$grouped р."
}

private fun Int.toStars(): String {
    val safe = coerceIn(1, 5)
    return buildString {
        repeat(safe) { append('★') }
        repeat(5 - safe) { append('☆') }
    }
}

private fun Gym.formatSchedule(): List<String> {
    val entriesByDay = schedule.associateBy { it.dayOfWeek }
    return listOf(1, 2, 3, 4, 5, 6, 7).map { day ->
        val dayLabel = when (day) {
            1 -> "Пн"
            2 -> "Вт"
            3 -> "Ср"
            4 -> "Чт"
            5 -> "Пт"
            6 -> "Сб"
            else -> "Вс"
        }

        val entry = entriesByDay[day] ?: entriesByDay[day - 1]
        val hours = if (entry?.openTime != null && entry.closeTime != null) {
            "${entry.openTime.take(5)} - ${entry.closeTime.take(5)}"
        } else {
            "не указано"
        }

        "$dayLabel: $hours"
    }
}

@Composable
private fun GymImagePlaceholder() {
    Box(
        modifier = Modifier
            .size(98.dp)
            .background(Color(0xFF4F4B41), RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.FitnessCenter,
            contentDescription = null,
            tint = ScreenBg.copy(alpha = 0.92f),
            modifier = Modifier.size(40.dp)
        )
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
                isSelected = selectedTab == ClientTab.Activities,
                icon = {
                    Icon(
                        imageVector = ClientBottomIcons.Activities,
                        contentDescription = "Activities"
                    )
                },
                onClick = { onTabSelected(ClientTab.Activities) }
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
                isSelected = selectedTab == ClientTab.Refresh,
                icon = {
                    Icon(
                        imageVector = ClientBottomIcons.Refresh,
                        contentDescription = "Refresh"
                    )
                },
                onClick = { onTabSelected(ClientTab.Refresh) }
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

private object ClientBottomIcons {
    val Activities: ImageVector
        get() = ImageVector.Builder(
            name = "ClientActivities",
            defaultWidth = 18.dp,
            defaultHeight = 18.dp,
            viewportWidth = 18f,
            viewportHeight = 18f
        ).apply {
            path(fill = SolidColor(Color(0xFF1C1B1F))) {
                moveTo(2f, 18f)
                curveTo(1.45f, 18f, 0.979167f, 17.8042f, 0.5875f, 17.4125f)
                curveTo(0.195833f, 17.0208f, 0f, 16.55f, 0f, 16f)
                verticalLineTo(2f)
                curveTo(0f, 1.45f, 0.195833f, 0.979167f, 0.5875f, 0.5875f)
                curveTo(0.979167f, 0.195833f, 1.45f, 0f, 2f, 0f)
                horizontalLineTo(16f)
                curveTo(16.55f, 0f, 17.0208f, 0.195833f, 17.4125f, 0.5875f)
                curveTo(17.8042f, 0.979167f, 18f, 1.45f, 18f, 2f)
                verticalLineTo(16f)
                curveTo(18f, 16.55f, 17.8042f, 17.0208f, 17.4125f, 17.4125f)
                curveTo(17.0208f, 17.8042f, 16.55f, 18f, 16f, 18f)
                horizontalLineTo(2f)
                close()
                moveTo(2f, 16f)
                horizontalLineTo(16f)
                verticalLineTo(5f)
                horizontalLineTo(2f)
                verticalLineTo(16f)
                close()
            }
        }.build()

    val Notifications: ImageVector
        get() = ImageVector.Builder(
            name = "ClientNotifications",
            defaultWidth = 16.dp,
            defaultHeight = 20.dp,
            viewportWidth = 16f,
            viewportHeight = 20f
        ).apply {
            path(fill = SolidColor(Color(0xFF1C1B1F))) {
                moveTo(0f, 17f)
                verticalLineTo(15f)
                horizontalLineTo(2f)
                verticalLineTo(8f)
                curveTo(2f, 6.61667f, 2.41667f, 5.3875f, 3.25f, 4.3125f)
                curveTo(4.08333f, 3.2375f, 5.16667f, 2.53333f, 6.5f, 2.2f)
                verticalLineTo(1.5f)
                curveTo(6.5f, 1.08333f, 6.64583f, 0.729167f, 6.9375f, 0.4375f)
                curveTo(7.22917f, 0.145833f, 7.58333f, 0f, 8f, 0f)
                curveTo(8.41667f, 0f, 8.77083f, 0.145833f, 9.0625f, 0.4375f)
                curveTo(9.35417f, 0.729167f, 9.5f, 1.08333f, 9.5f, 1.5f)
                verticalLineTo(2.2f)
                curveTo(10.8333f, 2.53333f, 11.9167f, 3.2375f, 12.75f, 4.3125f)
                curveTo(13.5833f, 5.3875f, 14f, 6.61667f, 14f, 8f)
                verticalLineTo(15f)
                horizontalLineTo(16f)
                verticalLineTo(17f)
                horizontalLineTo(0f)
                close()
                moveTo(8f, 20f)
                curveTo(7.45f, 20f, 6.97917f, 19.8042f, 6.5875f, 19.4125f)
                curveTo(6.19583f, 19.0208f, 6f, 18.55f, 6f, 18f)
                horizontalLineTo(10f)
                curveTo(10f, 18.55f, 9.80417f, 19.0208f, 9.4125f, 19.4125f)
                curveTo(9.02083f, 19.8042f, 8.55f, 20f, 8f, 20f)
                close()
                moveTo(4f, 15f)
                horizontalLineTo(12f)
                verticalLineTo(8f)
                curveTo(12f, 6.9f, 11.6083f, 5.95833f, 10.825f, 5.175f)
                curveTo(10.0417f, 4.39167f, 9.1f, 4f, 8f, 4f)
                curveTo(6.9f, 4f, 5.95833f, 4.39167f, 5.175f, 5.175f)
                curveTo(4.39167f, 5.95833f, 4f, 6.9f, 4f, 8f)
                verticalLineTo(15f)
                close()
            }
        }.build()

    val Refresh: ImageVector
        get() = ImageVector.Builder(
            name = "ClientRefresh",
            defaultWidth = 20.dp,
            defaultHeight = 20.dp,
            viewportWidth = 20f,
            viewportHeight = 20f
        ).apply {
            path(fill = SolidColor(Color(0xFF1C1B1F))) {
                moveTo(9f, 19.9f)
                curveTo(6.43333f, 19.65f, 4.29167f, 18.575f, 2.575f, 16.675f)
                curveTo(0.858333f, 14.775f, 0f, 12.5333f, 0f, 9.95f)
                curveTo(0f, 7.36667f, 0.858333f, 5.125f, 2.575f, 3.225f)
                curveTo(4.29167f, 1.325f, 6.43333f, 0.25f, 9f, 0f)
                verticalLineTo(3f)
                curveTo(7.26667f, 3.23333f, 5.83333f, 4.00833f, 4.7f, 5.325f)
                curveTo(3.56667f, 6.64167f, 3f, 8.18333f, 3f, 9.95f)
                curveTo(3f, 11.7167f, 3.56667f, 13.2583f, 4.7f, 14.575f)
                curveTo(5.83333f, 15.8917f, 7.26667f, 16.6667f, 9f, 16.9f)
                verticalLineTo(19.9f)
                close()
                moveTo(11f, 19.9f)
                verticalLineTo(16.9f)
                curveTo(12.5667f, 16.7f, 13.8917f, 16.05f, 14.975f, 14.95f)
                curveTo(16.0583f, 13.85f, 16.7167f, 12.5167f, 16.95f, 10.95f)
                horizontalLineTo(19.95f)
                curveTo(19.7167f, 13.3333f, 18.7625f, 15.3625f, 17.0875f, 17.0375f)
                curveTo(15.4125f, 18.7125f, 13.3833f, 19.6667f, 11f, 19.9f)
                close()
                moveTo(16.95f, 8.95f)
                curveTo(16.7167f, 7.38333f, 16.0583f, 6.05f, 14.975f, 4.95f)
                curveTo(13.8917f, 3.85f, 12.5667f, 3.2f, 11f, 3f)
                verticalLineTo(0f)
                curveTo(13.3833f, 0.233333f, 15.4125f, 1.1875f, 17.0875f, 2.8625f)
                curveTo(18.7625f, 4.5375f, 19.7167f, 6.56667f, 19.95f, 8.95f)
                horizontalLineTo(16.95f)
                close()
            }
        }.build()

    val Profile: ImageVector
        get() = ImageVector.Builder(
            name = "ClientProfile",
            defaultWidth = 21.dp,
            defaultHeight = 21.dp,
            viewportWidth = 21f,
            viewportHeight = 21f
        ).apply {
            path(fill = SolidColor(Color(0xFF1C1B1F))) {
                moveTo(4.01042f, 15.7292f)
                curveTo(4.89583f, 15.0521f, 5.88542f, 14.5182f, 6.97917f, 14.1276f)
                curveTo(8.07292f, 13.737f, 9.21875f, 13.5417f, 10.4167f, 13.5417f)
                curveTo(11.6146f, 13.5417f, 12.7604f, 13.737f, 13.8542f, 14.1276f)
                curveTo(14.9479f, 14.5182f, 15.9375f, 15.0521f, 16.8229f, 15.7292f)
                curveTo(17.4306f, 15.0174f, 17.9036f, 14.2101f, 18.2422f, 13.3073f)
                curveTo(18.5807f, 12.4045f, 18.75f, 11.441f, 18.75f, 10.4167f)
                curveTo(18.75f, 8.10764f, 17.9384f, 6.14149f, 16.3151f, 4.51823f)
                curveTo(14.6918f, 2.89497f, 12.7257f, 2.08333f, 10.4167f, 2.08333f)
                curveTo(8.10764f, 2.08333f, 6.14149f, 2.89497f, 4.51823f, 4.51823f)
                curveTo(2.89497f, 6.14149f, 2.08333f, 8.10764f, 2.08333f, 10.4167f)
                curveTo(2.08333f, 11.441f, 2.2526f, 12.4045f, 2.59115f, 13.3073f)
                curveTo(2.92969f, 14.2101f, 3.40278f, 15.0174f, 4.01042f, 15.7292f)
                close()
                moveTo(10.4167f, 11.4583f)
                curveTo(9.39236f, 11.4583f, 8.52865f, 11.1068f, 7.82552f, 10.4036f)
                curveTo(7.1224f, 9.70052f, 6.77083f, 8.83681f, 6.77083f, 7.8125f)
                curveTo(6.77083f, 6.7882f, 7.1224f, 5.92448f, 7.82552f, 5.22135f)
                curveTo(8.52865f, 4.51823f, 9.39236f, 4.16667f, 10.4167f, 4.16667f)
                curveTo(11.441f, 4.16667f, 12.3047f, 4.51823f, 13.0078f, 5.22135f)
                curveTo(13.7109f, 5.92448f, 14.0625f, 6.7882f, 14.0625f, 7.8125f)
                curveTo(14.0625f, 8.83681f, 13.7109f, 9.70052f, 13.0078f, 10.4036f)
                curveTo(12.3047f, 11.1068f, 11.441f, 11.4583f, 10.4167f, 11.4583f)
                close()
                moveTo(10.4167f, 20.8333f)
                curveTo(8.9757f, 20.8333f, 7.62153f, 20.5599f, 6.35417f, 20.013f)
                curveTo(5.08681f, 19.4661f, 3.98438f, 18.724f, 3.04688f, 17.7865f)
                curveTo(2.10938f, 16.849f, 1.36719f, 15.7465f, 0.820313f, 14.4792f)
                curveTo(0.273438f, 13.2118f, 0f, 11.8576f, 0f, 10.4167f)
                curveTo(0f, 8.9757f, 0.273438f, 7.62153f, 0.820313f, 6.35417f)
                curveTo(1.36719f, 5.08681f, 2.10938f, 3.98438f, 3.04688f, 3.04688f)
                curveTo(3.98438f, 2.10938f, 5.08681f, 1.36719f, 6.35417f, 0.820313f)
                curveTo(7.62153f, 0.273438f, 8.9757f, 0f, 10.4167f, 0f)
                curveTo(11.8576f, 0f, 13.2118f, 0.273438f, 14.4792f, 0.820313f)
                curveTo(15.7465f, 1.36719f, 16.849f, 2.10938f, 17.7865f, 3.04688f)
                curveTo(18.724f, 3.98438f, 19.4661f, 5.08681f, 20.013f, 6.35417f)
                curveTo(20.5599f, 7.62153f, 20.8333f, 8.9757f, 20.8333f, 10.4167f)
                curveTo(20.8333f, 11.8576f, 20.5599f, 13.2118f, 20.013f, 14.4792f)
                curveTo(19.4661f, 15.7465f, 18.724f, 16.849f, 17.7865f, 17.7865f)
                curveTo(16.849f, 18.724f, 15.7465f, 19.4661f, 14.4792f, 20.013f)
                curveTo(13.2118f, 20.5599f, 11.8576f, 20.8333f, 10.4167f, 20.8333f)
                close()
                moveTo(10.4167f, 18.75f)
                curveTo(11.3368f, 18.75f, 12.2049f, 18.6155f, 13.0208f, 18.3464f)
                curveTo(13.8368f, 18.0773f, 14.5833f, 17.691f, 15.2604f, 17.1875f)
                curveTo(14.5833f, 16.684f, 13.8368f, 16.2977f, 13.0208f, 16.0286f)
                curveTo(12.2049f, 15.7596f, 11.3368f, 15.625f, 10.4167f, 15.625f)
                curveTo(9.49653f, 15.625f, 8.62847f, 15.7596f, 7.8125f, 16.0286f)
                curveTo(6.99653f, 16.2977f, 6.25f, 16.684f, 5.57292f, 17.1875f)
                curveTo(6.25f, 17.691f, 6.99653f, 18.0773f, 7.8125f, 18.3464f)
                curveTo(8.62847f, 18.6155f, 9.49653f, 18.75f, 10.4167f, 18.75f)
                close()
                moveTo(10.4167f, 9.375f)
                curveTo(10.8681f, 9.375f, 11.2413f, 9.22743f, 11.5365f, 8.93229f)
                curveTo(11.8316f, 8.63715f, 11.9792f, 8.26389f, 11.9792f, 7.8125f)
                curveTo(11.9792f, 7.36111f, 11.8316f, 6.98785f, 11.5365f, 6.69271f)
                curveTo(11.2413f, 6.39757f, 10.8681f, 6.25f, 10.4167f, 6.25f)
                curveTo(9.96528f, 6.25f, 9.59202f, 6.39757f, 9.29688f, 6.69271f)
                curveTo(9.00174f, 6.98785f, 8.85417f, 7.36111f, 8.85417f, 7.8125f)
                curveTo(8.85417f, 8.26389f, 9.00174f, 8.63715f, 9.29688f, 8.93229f)
                curveTo(9.59202f, 9.22743f, 9.96528f, 9.375f, 10.4167f, 9.375f)
                close()
            }
        }.build()
}

