package com.simple.sportly.ui.screen.client

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
internal fun MarketplaceTab(
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
                activeMembership = state.activeMembership,
                activePackage = state.activePackage,
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
    activeMembership: ActiveMembership?,
    activePackage: ActivePackage?,
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
            GymPricesTab.Memberships -> MembershipsList(
                items = gym.membershipTypes,
                activeMembership = activeMembership
            )

            GymPricesTab.Packages -> TrainerPackagesList(
                items = gym.trainerPackages,
                activePackage = activePackage
            )
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
private fun MembershipsList(
    items: List<GymMembershipType>,
    activeMembership: ActiveMembership?
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        if (activeMembership != null) {
            item(key = "active-membership") {
                ActiveServiceCard(
                    title = "Активное членство",
                    description = activeMembership.membershipTypeName
                )
            }
        }

        if (items.isEmpty()) {
            item(key = "empty-memberships") {
                Text(
                    text = "Услуги не найдены",
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
            items(items, key = { it.id }) { membership ->
                ServiceCard(
                    title = membership.name,
                    description = membership.description,
                    priceText = membership.price.toRubPrice()
                )
            }
        }
    }
}

@Composable
private fun TrainerPackagesList(
    items: List<GymTrainerPackage>,
    activePackage: ActivePackage?
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        if (activePackage != null) {
            item(key = "active-package") {
                val trainerName = listOfNotNull(
                    activePackage.trainerLastName?.takeIf { it.isNotBlank() },
                    activePackage.trainerFirstName?.takeIf { it.isNotBlank() },
                    activePackage.trainerPatronymic?.takeIf { it.isNotBlank() }
                ).joinToString(" ").ifBlank { null }
                val description = buildString {
                    append(activePackage.trainerPackageName)
                    activePackage.trainerPackageDescription
                        ?.takeIf { it.isNotBlank() }
                        ?.let {
                            append(" â€¢ ")
                            append(it)
                        }
                    activePackage.sessionsLeft?.let { left ->
                        append(" • Осталось занятий: ")
                        append(left)
                    }
                    trainerName?.let { name ->
                        append(" • ")
                        append(name)
                    }
                }
                ActiveServiceCard(
                    title = "Активный пакет",
                    description = description
                )
            }
        }

        if (items.isEmpty()) {
            item(key = "empty-packages") {
                Text(
                    text = "Услуги не найдены",
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
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

@Composable
private fun ActiveServiceCard(
    title: String,
    description: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.bodyLarge
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

