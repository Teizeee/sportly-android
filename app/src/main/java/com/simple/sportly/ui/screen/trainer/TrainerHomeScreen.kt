package com.simple.sportly.ui.screen.trainer

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.simple.sportly.BuildConfig
import com.simple.sportly.domain.model.TrainerReview
import com.simple.sportly.domain.model.TrainerSlot
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.yield

private val ScreenBg = Color(0xFFE7E3DA)
private val CardBg = Color(0xFFD1CAB9)
private val AccentDark = Color(0xFF565347)
private val MainText = Color(0xFF29251F)
private val OpenButtonColor = Color(0xFF5B8A5A)
private val CloseButtonColor = Color(0xFFB6544A)

@Composable
fun TrainerHomeScreen(
    state: TrainerHomeUiState,
    onTabSelected: (TrainerTab) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPatronymicChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onRetryReviewsClick: () -> Unit,
    onScheduleTabShown: () -> Unit,
    onScheduleDateSelected: (LocalDate) -> Unit,
    onOpenDayClick: () -> Unit,
    onCloseDayClick: () -> Unit,
    onSlotAvailabilityClick: (TrainerSlot) -> Unit,
    onRetryScheduleClick: () -> Unit,
    onAvatarSelected: (Uri) -> Unit,
    onLogoutClick: () -> Unit
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            onAvatarSelected(uri)
        }
    }

    Scaffold(
        containerColor = ScreenBg,
        bottomBar = {
            TrainerBottomBar(
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
                TrainerTab.Profile -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = onLogoutClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = "Logout",
                                    tint = MainText
                                )
                            }
                        }

                        AvatarBlock(
                            imageUrl = buildAvatarUrl(
                                userId = state.userId,
                                avatarVersion = state.avatarVersion
                            ),
                            isLoading = state.isUploadingAvatar,
                            onClick = { imagePickerLauncher.launch("image/*") }
                        )

                        Spacer(modifier = Modifier.height(14.dp))

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
                        RoundedInput(
                            value = state.phone,
                            onValueChange = onPhoneChange,
                            placeholder = "Телефон",
                            keyboardType = KeyboardType.Phone
                        )
                        RoundedInput(
                            value = state.birthDate,
                            onValueChange = onBirthDateChange,
                            placeholder = "Дата рождения (YYYY-MM-DD)"
                        )
                        RoundedInput(
                            value = state.description,
                            onValueChange = onDescriptionChange,
                            placeholder = "Описание",
                            singleLine = false,
                            minLines = 6
                        )

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
                            enabled = !state.isSaving && !state.isUploadingAvatar,
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
                }

                TrainerTab.Schedule -> {
                    ScheduleTab(
                        state = state,
                        onScheduleTabShown = onScheduleTabShown,
                        onScheduleDateSelected = onScheduleDateSelected,
                        onOpenDayClick = onOpenDayClick,
                        onCloseDayClick = onCloseDayClick,
                        onSlotAvailabilityClick = onSlotAvailabilityClick,
                        onRetryScheduleClick = onRetryScheduleClick
                    )
                }

                TrainerTab.Awards -> {
                    ReviewsTab(
                        state = state,
                        onRetryClick = onRetryReviewsClick
                    )
                    return@Box
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleTab(
    state: TrainerHomeUiState,
    onScheduleTabShown: () -> Unit,
    onScheduleDateSelected: (LocalDate) -> Unit,
    onOpenDayClick: () -> Unit,
    onCloseDayClick: () -> Unit,
    onSlotAvailabilityClick: (TrainerSlot) -> Unit,
    onRetryScheduleClick: () -> Unit
) {
    val isPastDate = state.selectedScheduleDate.isBefore(LocalDate.now())
    var shouldRenderCalendar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onScheduleTabShown()
        // Let the tab render first to avoid blocking navigation transition.
        yield()
        shouldRenderCalendar = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        if (shouldRenderCalendar) {
            val selectedDateMillis = state.selectedScheduleDate.toEpochMillis()
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)

            LaunchedEffect(state.selectedScheduleDate) {
                if (datePickerState.selectedDateMillis != selectedDateMillis) {
                    datePickerState.selectedDateMillis = selectedDateMillis
                }
            }

            LaunchedEffect(datePickerState) {
                snapshotFlow { datePickerState.selectedDateMillis }
                    .filterNotNull()
                    .map { it.toLocalDate() }
                    .distinctUntilChanged()
                    .collect(onScheduleDateSelected)
            }

            Surface(
                color = CardBg,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.fillMaxWidth(),
                    title = null,
                    headline = null,
                    showModeToggle = false,
                    colors = androidx.compose.material3.DatePickerDefaults.colors(
                        containerColor = CardBg,
                        titleContentColor = MainText,
                        headlineContentColor = MainText,
                        weekdayContentColor = MainText,
                        subheadContentColor = MainText,
                        yearContentColor = MainText,
                        currentYearContentColor = AccentDark,
                        selectedYearContainerColor = AccentDark,
                        selectedYearContentColor = ScreenBg,
                        dayContentColor = MainText,
                        disabledDayContentColor = MainText.copy(alpha = 0.35f),
                        selectedDayContainerColor = AccentDark,
                        selectedDayContentColor = ScreenBg,
                        todayContentColor = AccentDark,
                        todayDateBorderColor = AccentDark,
                        navigationContentColor = MainText
                    )
                )
            }
        } else {
            Surface(
                color = CardBg,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AccentDark)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            color = CardBg,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                Text(
                    text = formatScheduleDate(state.selectedScheduleDate),
                    color = MainText,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onCloseDayClick,
                        enabled = !state.isScheduleActionLoading && !isPastDate,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CloseButtonColor,
                            contentColor = ScreenBg
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Закрыть", fontFamily = FontFamily.Serif)
                    }
                    Button(
                        onClick = onOpenDayClick,
                        enabled = !state.isScheduleActionLoading && !isPastDate,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OpenButtonColor,
                            contentColor = ScreenBg
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Открыть", fontFamily = FontFamily.Serif)
                    }
                }
            }
        }

        if (!state.scheduleErrorMessage.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = state.scheduleErrorMessage,
                color = MaterialTheme.colorScheme.error,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onRetryScheduleClick,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentDark,
                    contentColor = ScreenBg
                )
            ) {
                Text("Повторить", fontFamily = FontFamily.Serif)
            }
        }

        if (!state.scheduleInfoMessage.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.scheduleInfoMessage,
                color = AccentDark,
                fontFamily = FontFamily.Serif
            )
        }

        if (state.isScheduleLoading || state.isScheduleActionLoading) {
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = AccentDark,
                trackColor = CardBg.copy(alpha = 0.6f)
            )
        }

        if (isPastDate) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Прошедшая дата доступна только для просмотра",
                color = MainText.copy(alpha = 0.72f),
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Serif
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            color = CardBg.copy(alpha = 0.55f),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                Text(
                    text = "Слоты на день",
                    color = MainText,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MainText.copy(alpha = 0.18f)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (state.isScheduleLoading && state.slots.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AccentDark)
            }
            return
        }

        if (state.slots.isEmpty()) {
            Text(
                text = "Слотов на выбранный день нет",
                color = MainText,
                fontFamily = FontFamily.Serif
            )
            return
        }

        state.slots.forEach { slot ->
            ScheduleSlotCard(
                slot = slot,
                actionEnabled = !state.isScheduleActionLoading && !isPastDate,
                onSlotAvailabilityClick = onSlotAvailabilityClick
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ScheduleSlotCard(
    slot: TrainerSlot,
    actionEnabled: Boolean,
    onSlotAvailabilityClick: (TrainerSlot) -> Unit
) {
    Surface(
        color = CardBg,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
            Text(
                text = "${formatSlotTime(slot.startTime)} - ${formatSlotTime(slot.endTime)}",
                color = MainText,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Serif
            )

            val bookedName = formatBookedUserName(slot)
            if (bookedName != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = bookedName,
                    color = MainText,
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily.Serif
                )
            }

            val bookingStateLabel = bookingStateLabel(slot.bookingStatus)
            if (!bookingStateLabel.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = bookingStateLabel,
                    color = MainText.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.Serif
                )
            }

            val buttonLabel = when {
                slot.id == null -> "Сделать доступным"
                slot.bookingId == null -> "Сделать недоступным"
                else -> null
            }

            if (buttonLabel != null) {
                val actionColor = if (slot.id == null) OpenButtonColor else CloseButtonColor
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { onSlotAvailabilityClick(slot) },
                    enabled = actionEnabled,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = actionColor,
                        contentColor = ScreenBg
                    )
                ) {
                    Text(buttonLabel, fontFamily = FontFamily.Serif)
                }
            }
        }
    }
}

@Composable
private fun ReviewsTab(
    state: TrainerHomeUiState,
    onRetryClick: () -> Unit
) {
    if (state.isReviewsLoading && state.reviews.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = AccentDark)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Рейтинг: ${formatAverageRating(state.reviews)}",
            color = MainText,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (!state.reviewsErrorMessage.isNullOrBlank()) {
            Text(
                text = state.reviewsErrorMessage,
                color = MaterialTheme.colorScheme.error,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onRetryClick,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentDark,
                    contentColor = ScreenBg
                )
            ) {
                Text("Повторить", fontFamily = FontFamily.Serif)
            }
            return
        }

        if (state.reviews.isEmpty()) {
            Text(
                text = "Отзывов пока нет",
                color = MainText,
                fontFamily = FontFamily.Serif
            )
            return
        }

        state.reviews.forEach { review ->
            ReviewCard(
                authorName = review.authorFullName,
                rating = review.rating,
                comment = review.comment.orEmpty()
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
private fun ReviewCard(
    authorName: String,
    rating: Int,
    comment: String
) {
    Surface(
        color = CardBg,
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = authorName,
                    color = MainText,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                    fontFamily = FontFamily.Serif
                )
                RatingStars(rating = rating)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = comment,
                color = MainText,
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = FontFamily.Serif
            )
        }
    }
}

@Composable
private fun RatingStars(rating: Int) {
    val normalizedRating = rating.coerceIn(0, 5)
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(5) { index ->
            Text(
                text = if (index < normalizedRating) "★" else "☆",
                color = MainText,
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                lineHeight = 22.sp
            )
        }
    }
}

private fun formatAverageRating(reviews: List<TrainerReview>): String {
    if (reviews.isEmpty()) return "0,0"
    val average = reviews.map { it.rating.coerceIn(0, 5) }.average()
    return String.format(Locale.US, "%.1f", average).replace('.', ',')
}

private fun formatScheduleDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
    return date.format(formatter)
}

private fun formatSlotTime(value: String): String {
    return runCatching {
        LocalDateTime.parse(value).format(DateTimeFormatter.ofPattern("HH:mm"))
    }.getOrElse {
        value.substringAfter('T', value).take(5)
    }
}

private fun formatBookedUserName(slot: TrainerSlot): String? {
    val user = slot.bookedUser ?: return null
    return listOf(user.firstName.trim(), user.lastName.trim())
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .ifBlank { null }
}

private fun bookingStateLabel(status: String?): String? {
    return when (status) {
        "VISITED" -> "Посещено"
        "NOT_VISITED" -> "Не посещено"
        else -> null
    }
}

private fun LocalDate.toEpochMillis(): Long {
    return atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}

private fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
}

@Composable
private fun RoundedInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        minLines = minLines,
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
            .padding(vertical = 5.dp)
    )
}

@Composable
private fun AvatarBlock(
    imageUrl: String?,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(160.dp)
            .clip(CircleShape)
            .background(CardBg)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(30.dp), color = AccentDark)
            return
        }

        if (imageUrl.isNullOrBlank()) {
            DefaultAvatarIcon()
            return
        }

        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = "Trainer avatar",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier.size(26.dp),
                    color = AccentDark
                )
            },
            error = { DefaultAvatarIcon() }
        )
    }
}

@Composable
private fun DefaultAvatarIcon() {
    Icon(
        imageVector = Icons.Default.Person,
        contentDescription = "Default avatar",
        tint = AccentDark,
        modifier = Modifier.size(82.dp)
    )
}

@Composable
private fun TrainerBottomBar(
    selectedTab: TrainerTab,
    onTabSelected: (TrainerTab) -> Unit
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
                isSelected = selectedTab == TrainerTab.Schedule,
                icon = {
                    Icon(
                        imageVector = TrainerBottomIcons.Schedule,
                        contentDescription = "Schedule"
                    )
                },
                onClick = { onTabSelected(TrainerTab.Schedule) }
            )
            BottomBarItem(
                isSelected = selectedTab == TrainerTab.Awards,
                icon = {
                    Icon(
                        imageVector = TrainerBottomIcons.Awards,
                        contentDescription = "Awards"
                    )
                },
                onClick = { onTabSelected(TrainerTab.Awards) }
            )
            BottomBarItem(
                isSelected = selectedTab == TrainerTab.Profile,
                icon = {
                    Icon(
                        imageVector = TrainerBottomIcons.Profile,
                        contentDescription = "Profile"
                    )
                },
                onClick = { onTabSelected(TrainerTab.Profile) }
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
            .clip(CircleShape)
            .background(AccentDark)
    } else {
        Modifier.size(52.dp)
    }

    Box(
        modifier = itemModifier.clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.runtime.CompositionLocalProvider(
            androidx.compose.material3.LocalContentColor provides contentColor
        ) {
            icon()
        }
    }
}

private fun buildAvatarUrl(userId: String, avatarVersion: Long): String? {
    if (userId.isBlank()) return null
    val baseUrl = BuildConfig.BASE_URL.trimEnd('/')
    return "$baseUrl/avatars/$userId.jpg?v=$avatarVersion"
}

private object TrainerBottomIcons {
    val Schedule: ImageVector
        get() = ImageVector.Builder(
            name = "ScheduleFromSvg",
            defaultWidth = 19.dp,
            defaultHeight = 20.dp,
            viewportWidth = 19f,
            viewportHeight = 20f
        ).apply {
            path(fill = SolidColor(Color(0xFF1C1B1F))) {
                moveTo(14f, 20f)
                verticalLineTo(17f)
                horizontalLineTo(11f)
                verticalLineTo(15f)
                horizontalLineTo(14f)
                verticalLineTo(12f)
                horizontalLineTo(16f)
                verticalLineTo(15f)
                horizontalLineTo(19f)
                verticalLineTo(17f)
                horizontalLineTo(16f)
                verticalLineTo(20f)
                horizontalLineTo(14f)
                close()
                moveTo(2f, 18f)
                curveTo(1.45f, 18f, 0.979167f, 17.8042f, 0.5875f, 17.4125f)
                curveTo(0.195833f, 17.0208f, 0f, 16.55f, 0f, 16f)
                verticalLineTo(4f)
                curveTo(0f, 3.45f, 0.195833f, 2.97917f, 0.5875f, 2.5875f)
                curveTo(0.979167f, 2.19583f, 1.45f, 2f, 2f, 2f)
                horizontalLineTo(3f)
                verticalLineTo(0f)
                horizontalLineTo(5f)
                verticalLineTo(2f)
                horizontalLineTo(11f)
                verticalLineTo(0f)
                horizontalLineTo(13f)
                verticalLineTo(2f)
                horizontalLineTo(14f)
                curveTo(14.55f, 2f, 15.0208f, 2.19583f, 15.4125f, 2.5875f)
                curveTo(15.8042f, 2.97917f, 16f, 3.45f, 16f, 4f)
                verticalLineTo(10.1f)
                curveTo(15.6667f, 10.05f, 15.3333f, 10.025f, 15f, 10.025f)
                curveTo(14.6667f, 10.025f, 14.3333f, 10.05f, 14f, 10.1f)
                verticalLineTo(8f)
                horizontalLineTo(2f)
                verticalLineTo(16f)
                horizontalLineTo(9f)
                curveTo(9f, 16.3333f, 9.025f, 16.6667f, 9.075f, 17f)
                curveTo(9.125f, 17.3333f, 9.21667f, 17.6667f, 9.35f, 18f)
                horizontalLineTo(2f)
                close()
                moveTo(2f, 6f)
                horizontalLineTo(14f)
                verticalLineTo(4f)
                horizontalLineTo(2f)
                verticalLineTo(6f)
                close()
            }
        }.build()

    val Awards: ImageVector
        get() = ImageVector.Builder(
            name = "AwardsFromSvg",
            defaultWidth = 21.dp,
            defaultHeight = 19.dp,
            viewportWidth = 21f,
            viewportHeight = 19f
        ).apply {
            path(fill = SolidColor(Color(0xFF1C1B1F))) {
                moveTo(12.1375f, 13.2875f)
                curveTo(12.7625f, 12.8125f, 13.2167f, 12.2f, 13.5f, 11.45f)
                horizontalLineTo(6.5f)
                curveTo(6.78333f, 12.2f, 7.2375f, 12.8125f, 7.8625f, 13.2875f)
                curveTo(8.4875f, 13.7625f, 9.2f, 14f, 10f, 14f)
                curveTo(10.8f, 14f, 11.5125f, 13.7625f, 12.1375f, 13.2875f)
                close()
                moveTo(8.5625f, 9.5625f)
                curveTo(8.85417f, 9.27083f, 9f, 8.91667f, 9f, 8.5f)
                curveTo(9f, 8.08333f, 8.85417f, 7.72917f, 8.5625f, 7.4375f)
                curveTo(8.27083f, 7.14583f, 7.91667f, 7f, 7.5f, 7f)
                curveTo(7.08333f, 7f, 6.72917f, 7.14583f, 6.4375f, 7.4375f)
                curveTo(6.14583f, 7.72917f, 6f, 8.08333f, 6f, 8.5f)
                curveTo(6f, 8.91667f, 6.14583f, 9.27083f, 6.4375f, 9.5625f)
                curveTo(6.72917f, 9.85417f, 7.08333f, 10f, 7.5f, 10f)
                curveTo(7.91667f, 10f, 8.27083f, 9.85417f, 8.5625f, 9.5625f)
                close()
                moveTo(13.5625f, 9.5625f)
                curveTo(13.8542f, 9.27083f, 14f, 8.91667f, 14f, 8.5f)
                curveTo(14f, 8.08333f, 13.8542f, 7.72917f, 13.5625f, 7.4375f)
                curveTo(13.2708f, 7.14583f, 12.9167f, 7f, 12.5f, 7f)
                curveTo(12.0833f, 7f, 11.7292f, 7.14583f, 11.4375f, 7.4375f)
                curveTo(11.1458f, 7.72917f, 11f, 8.08333f, 11f, 8.5f)
                curveTo(11f, 8.91667f, 11.1458f, 9.27083f, 11.4375f, 9.5625f)
                curveTo(11.7292f, 9.85417f, 12.0833f, 10f, 12.5f, 10f)
                curveTo(12.9167f, 10f, 13.2708f, 9.85417f, 13.5625f, 9.5625f)
                close()
                moveTo(5.625f, 4.4f)
                lineTo(8.425f, 0.775f)
                curveTo(8.625f, 0.508333f, 8.8625f, 0.3125f, 9.1375f, 0.1875f)
                curveTo(9.4125f, 0.0625f, 9.7f, 0f, 10f, 0f)
                curveTo(10.3f, 0f, 10.5875f, 0.0625f, 10.8625f, 0.1875f)
                curveTo(11.1375f, 0.3125f, 11.375f, 0.508333f, 11.575f, 0.775f)
                lineTo(14.375f, 4.4f)
                lineTo(18.625f, 5.825f)
                curveTo(19.0583f, 5.95833f, 19.4f, 6.20417f, 19.65f, 6.5625f)
                curveTo(19.9f, 6.92083f, 20.025f, 7.31667f, 20.025f, 7.75f)
                curveTo(20.025f, 7.95f, 19.9958f, 8.15f, 19.9375f, 8.35f)
                curveTo(19.8792f, 8.55f, 19.7833f, 8.74167f, 19.65f, 8.925f)
                lineTo(16.9f, 12.825f)
                lineTo(17f, 16.925f)
                curveTo(17.0167f, 17.5083f, 16.825f, 18f, 16.425f, 18.4f)
                curveTo(16.025f, 18.8f, 15.5583f, 19f, 15.025f, 19f)
                curveTo(14.9917f, 19f, 14.8083f, 18.975f, 14.475f, 18.925f)
                lineTo(10f, 17.675f)
                lineTo(5.525f, 18.925f)
                curveTo(5.44167f, 18.9583f, 5.35f, 18.9792f, 5.25f, 18.9875f)
                curveTo(5.15f, 18.9958f, 5.05833f, 19f, 4.975f, 19f)
                curveTo(4.44167f, 19f, 3.975f, 18.8f, 3.575f, 18.4f)
                curveTo(3.175f, 18f, 2.98333f, 17.5083f, 3f, 16.925f)
                lineTo(3.1f, 12.8f)
                lineTo(0.375f, 8.925f)
                curveTo(0.241667f, 8.74167f, 0.145833f, 8.55f, 0.0875f, 8.35f)
                curveTo(0.0291667f, 8.15f, 0f, 7.95f, 0f, 7.75f)
                curveTo(0f, 7.33333f, 0.120833f, 6.94583f, 0.3625f, 6.5875f)
                curveTo(0.604167f, 6.22917f, 0.941667f, 5.975f, 1.375f, 5.825f)
                lineTo(5.625f, 4.4f)
                close()
                moveTo(6.85f, 6.125f)
                lineTo(2f, 7.725f)
                lineTo(5.1f, 12.2f)
                lineTo(5f, 16.975f)
                lineTo(10f, 15.6f)
                lineTo(15f, 17f)
                lineTo(14.9f, 12.2f)
                lineTo(18f, 7.775f)
                lineTo(13.15f, 6.125f)
                lineTo(10f, 2f)
                lineTo(6.85f, 6.125f)
                close()
            }
        }.build()

    val ReviewStar: ImageVector
        get() = ImageVector.Builder(
            name = "ReviewStarFromSvg",
            defaultWidth = 20.dp,
            defaultHeight = 19.dp,
            viewportWidth = 20f,
            viewportHeight = 19f
        ).apply {
            path(fill = SolidColor(Color(0xFF11120D))) {
                moveTo(6.85f, 14.825f)
                lineTo(10f, 12.925f)
                lineTo(13.15f, 14.85f)
                lineTo(12.325f, 11.25f)
                lineTo(15.1f, 8.85f)
                lineTo(11.45f, 8.525f)
                lineTo(10f, 5.125f)
                lineTo(8.55f, 8.5f)
                lineTo(4.9f, 8.825f)
                lineTo(7.675f, 11.25f)
                lineTo(6.85f, 14.825f)
                close()
                moveTo(3.825f, 19f)
                lineTo(5.45f, 11.975f)
                lineTo(0f, 7.25f)
                lineTo(7.2f, 6.625f)
                lineTo(10f, 0f)
                lineTo(12.8f, 6.625f)
                lineTo(20f, 7.25f)
                lineTo(14.55f, 11.975f)
                lineTo(16.175f, 19f)
                lineTo(10f, 15.275f)
                lineTo(3.825f, 19f)
                close()
            }
        }.build()

    val Profile: ImageVector
        get() = ImageVector.Builder(
            name = "ProfileFromSvg",
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
