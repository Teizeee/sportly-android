package com.simple.sportly.ui.screen.client

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.simple.sportly.domain.model.ActiveMembership
import com.simple.sportly.domain.model.ActivePackage
import com.simple.sportly.domain.model.ClientBooking
import com.simple.sportly.domain.model.ClientBookingStatus
import com.simple.sportly.domain.model.ClientMembership
import com.simple.sportly.domain.model.ClientMembershipStatus
import com.simple.sportly.domain.model.ClientProgress
import com.simple.sportly.domain.model.ClientTrainerPackage
import com.simple.sportly.domain.model.ClientTrainerPackageStatus
import com.simple.sportly.domain.model.TrainerSlot
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

@Composable
internal fun StatisticsTab(
    activeMembership: ActiveMembership?,
    activePackage: ActivePackage?,
    isLoading: Boolean,
    errorMessage: String?,
    onRetryClick: () -> Unit,
    onMembershipsClick: () -> Unit,
    onPackagesClick: () -> Unit,
    onWeightClick: () -> Unit,
    onMyTrainingsClick: () -> Unit
) {
    val membershipText = activeMembership?.membershipTypeName ?: "Нет активного членства"
    val packageText = activePackage?.trainerPackageName ?: "Нет активного пакета"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = AccentDark, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        if (!errorMessage.isNullOrBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = onRetryClick) {
                    Text("Повторить")
                }
            }
        }

        StatisticsMenuCard(
            title = "Членства",
            subtitle = membershipText,
            onClick = onMembershipsClick
        )
        StatisticsMenuCard(
            title = "Пакеты",
            subtitle = packageText,
            onClick = onPackagesClick
        )
        StatisticsMenuCard(
            title = "Вес",
            subtitle = "Калькулятор ИМТ",
            onClick = onWeightClick
        )
        StatisticsMenuCard(
            title = "Мои тренировки",
            subtitle = null,
            onClick = onMyTrainingsClick
        )
    }
}

@Composable
private fun StatisticsMenuCard(
    title: String,
    subtitle: String?,
    onClick: (() -> Unit)? = null
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        color = MainText,
                        fontFamily = FontFamily.Serif,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MainText
            )
        }
    }
}

@Composable
internal fun MyTrainingsStatisticsPage(
    selectedTab: MyTrainingsTab,
    upcomingBookings: List<ClientBooking>,
    pastBookings: List<ClientBooking>,
    isLoading: Boolean,
    errorMessage: String?,
    cancellingBookingId: String?,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    onTabSelected: (MyTrainingsTab) -> Unit,
    onCancelClick: (ClientBooking) -> Unit,
    onLeaveReviewClick: (ClientBooking) -> Unit
) {
    val listToShow = if (selectedTab == MyTrainingsTab.Upcoming) upcomingBookings else pastBookings
    var pendingCancelBooking by remember { mutableStateOf<ClientBooking?>(null) }
    var showTooLateCancelDialog by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .padding(bottom = 24.dp)
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
                text = "Мои тренировки",
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MyTrainingsTabButton(
                text = "Предстоящие",
                selected = selectedTab == MyTrainingsTab.Upcoming,
                onClick = { onTabSelected(MyTrainingsTab.Upcoming) },
                modifier = Modifier.weight(1f)
            )
            MyTrainingsTabButton(
                text = "Прошедшие",
                selected = selectedTab == MyTrainingsTab.Past,
                onClick = { onTabSelected(MyTrainingsTab.Past) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
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
                        Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = onRetryClick) {
                            Text("Повторить")
                        }
                    }
                }

                listToShow.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (selectedTab == MyTrainingsTab.Upcoming) {
                                "Нет предстоящих тренировок"
                            } else {
                                "Нет прошедших тренировок"
                            },
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
                        items(listToShow, key = { it.id }) { booking ->
                            MyTrainingCard(
                                booking = booking,
                                isUpcomingTab = selectedTab == MyTrainingsTab.Upcoming,
                                cancellingBookingId = cancellingBookingId,
                                onCancelClick = { clicked ->
                                    if (isCancellationAllowed(clicked)) {
                                        pendingCancelBooking = clicked
                                    } else {
                                        showTooLateCancelDialog = true
                                    }
                                },
                                onLeaveReviewClick = onLeaveReviewClick
                            )
                        }
                    }
                }
            }
        }
    }

    if (pendingCancelBooking != null) {
        ActivationConfirmDialog(
            message = "\u0412\u044b \u0442\u043e\u0447\u043d\u043e \u0445\u043e\u0442\u0438\u0442\u0435 \u043e\u0442\u043c\u0435\u043d\u0438\u0442\u044c \u0437\u0430\u043d\u044f\u0442\u0438\u0435?",
            onConfirm = {
                val booking = pendingCancelBooking ?: return@ActivationConfirmDialog
                pendingCancelBooking = null
                onCancelClick(booking)
            },
            onDismiss = { pendingCancelBooking = null }
        )
    }

    if (showTooLateCancelDialog) {
        InfoAlertDialog(
            message = "\u041d\u0435\u043b\u044c\u0437\u044f \u043e\u0442\u043c\u0435\u043d\u0438\u0442\u044c \u0442\u0440\u0435\u043d\u0438\u0440\u043e\u0432\u043a\u0443 \u043f\u043e\u0437\u0436\u0435 \u0447\u0435\u043c \u0437\u0430 2 \u0447\u0430\u0441\u0430 \u0434\u043e \u0435\u0451 \u043d\u0430\u0447\u0430\u043b\u0430!",
            onDismiss = { showTooLateCancelDialog = false }
        )
    }
}

@Composable
private fun MyTrainingsTabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (selected) AccentDark else CardBg
    val textColor = if (selected) ScreenBg else MainText
    Button(
        onClick = onClick,
        modifier = modifier.height(34.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
    ) {
        Text(
            text = text,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun MyTrainingCard(
    booking: ClientBooking,
    isUpcomingTab: Boolean,
    cancellingBookingId: String?,
    onCancelClick: (ClientBooking) -> Unit,
    onLeaveReviewClick: (ClientBooking) -> Unit
) {
    val timeLabel = formatBookingDateTime(
        date = booking.date,
        startTime = booking.startTime,
        endTime = booking.endTime
    )
    val trainerName = listOfNotNull(
        booking.trainerFirstName.takeIf { it.isNotBlank() },
        booking.trainerLastName.takeIf { it.isNotBlank() },
        booking.trainerPatronymic?.takeIf { it.isNotBlank() }
    ).joinToString(" ").ifBlank {
        ""
    }

    val isCancelling = cancellingBookingId == booking.id

    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = timeLabel,
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = booking.gymTitle,
                color = MainText.copy(alpha = 0.72f),
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = if (trainerName.isBlank()) "Тренер" else "Тренер $trainerName",
                color = MainText.copy(alpha = 0.72f),
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                if (isUpcomingTab) {
                    TextButton(
                        onClick = { onCancelClick(booking) },
                        enabled = !isCancelling,
                        colors = ButtonDefaults.textButtonColors(contentColor = MainText),
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = if (isCancelling) "\u041e\u0442\u043c\u0435\u043d\u0430..." else "\u041e\u0442\u043c\u0435\u043d\u0438\u0442\u044c",
                            fontFamily = FontFamily.Serif,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else if (booking.status == ClientBookingStatus.VISITED) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { onLeaveReviewClick(booking) },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentDark,
                                contentColor = ScreenBg
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(
                                text = "Оставить отзыв",
                                fontFamily = FontFamily.Serif,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = booking.status.toStatusText(),
                            color = MainText.copy(alpha = 0.5f),
                            fontFamily = FontFamily.Serif,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(
                        text = booking.status.toStatusText(),
                        color = MainText.copy(alpha = 0.5f),
                        fontFamily = FontFamily.Serif,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun isCancellationAllowed(booking: ClientBooking): Boolean {
    val bookingStart = parseBookingStartDateTime(booking) ?: return false
    val now = LocalDateTime.now()
    return !bookingStart.isBefore(now.plusHours(2))
}

private fun parseBookingStartDateTime(booking: ClientBooking): LocalDateTime? {
    return runCatching {
        val date = LocalDate.parse(booking.date)
        val time = runCatching {
            LocalTime.parse(booking.startTime, DateTimeFormatter.ISO_LOCAL_TIME)
        }.getOrElse {
            LocalTime.parse(booking.startTime.take(5), DateTimeFormatter.ofPattern("HH:mm"))
        }
        LocalDateTime.of(date, time)
    }.getOrNull()
}

@Composable
private fun InfoAlertDialog(
    message: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = ScreenBg),
            shape = RoundedCornerShape(2.dp),
            border = BorderStroke(2.dp, Color.Black),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = message,
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentDark,
                        contentColor = ScreenBg
                    ),
                    modifier = Modifier
                        .size(width = 180.dp, height = 58.dp)
                ) {
                    Text(
                        text = "\u041e\u043a",
                        fontFamily = FontFamily.Serif,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    }
}

@Composable
internal fun TrainerReviewFormPage(
    booking: ClientBooking?,
    rating: Int,
    comment: String,
    leaveGymReview: Boolean,
    isSubmitting: Boolean,
    errorMessage: String?,
    onCloseClick: () -> Unit,
    onRatingChange: (Int) -> Unit,
    onCommentChange: (String) -> Unit,
    onLeaveGymReviewChange: (Boolean) -> Unit,
    onSubmitClick: () -> Unit
) {
    if (booking == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            TextButton(onClick = onCloseClick) {
                Text("Закрыть")
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Закрыть",
                    tint = MainText
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Как вам тренировка\nс ${booking.fullTrainerName()}?",
            color = MainText,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))
        RatingSelector(
            rating = rating,
            onRatingChange = onRatingChange,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(18.dp))
        ReviewCommentInput(
            comment = comment,
            onCommentChange = onCommentChange
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Оставить отзыв на зал?",
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Checkbox(
                checked = leaveGymReview,
                onCheckedChange = onLeaveGymReviewChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = AccentDark,
                    uncheckedColor = AccentDark
                )
            )
        }

        if (!errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onSubmitClick,
            enabled = rating in 1..5 && !isSubmitting,
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentDark,
                contentColor = ScreenBg
            ),
            modifier = Modifier
                .size(width = 160.dp, height = 52.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = ScreenBg,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Отправить",
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
internal fun GymReviewFormPage(
    booking: ClientBooking?,
    rating: Int,
    comment: String,
    isSubmitting: Boolean,
    errorMessage: String?,
    onCloseClick: () -> Unit,
    onRatingChange: (Int) -> Unit,
    onCommentChange: (String) -> Unit,
    onSubmitClick: () -> Unit
) {
    if (booking == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            TextButton(onClick = onCloseClick) {
                Text("Закрыть")
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Закрыть",
                    tint = MainText
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Как вам зал ${booking.gymTitle}?",
            color = MainText,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))
        RatingSelector(
            rating = rating,
            onRatingChange = onRatingChange,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(18.dp))
        ReviewCommentInput(
            comment = comment,
            onCommentChange = onCommentChange
        )

        if (!errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        Button(
            onClick = onSubmitClick,
            enabled = rating in 1..5 && !isSubmitting,
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentDark,
                contentColor = ScreenBg
            ),
            modifier = Modifier
                .size(width = 160.dp, height = 52.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = ScreenBg,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Отправить",
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
private fun RatingSelector(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) { index ->
            val starIndex = index + 1
            Icon(
                imageVector = if (starIndex <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = "Оценка $starIndex",
                tint = AccentDark,
                modifier = Modifier
                    .size(34.dp)
                    .clickable { onRatingChange(starIndex) }
            )
        }
    }
}

@Composable
private fun ReviewCommentInput(
    comment: String,
    onCommentChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = comment,
            onValueChange = { onCommentChange(it.take(255)) },
            minLines = 5,
            maxLines = 6,
            placeholder = {
                Text(
                    text = "Оставьте комментарий",
                    color = MainText.copy(alpha = 0.45f),
                    fontFamily = FontFamily.Serif
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MainText,
                fontFamily = FontFamily.Serif
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = ScreenBg,
                unfocusedContainerColor = ScreenBg,
                disabledContainerColor = ScreenBg,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = MainText
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MainText.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(12.dp)
                )
        )
        Text(
            text = "${comment.length}/255",
            color = MainText.copy(alpha = 0.55f),
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 4.dp, end = 6.dp)
        )
    }
}

private fun ClientBooking.fullTrainerName(): String {
    return listOfNotNull(
        trainerFirstName.takeIf { it.isNotBlank() },
        trainerLastName.takeIf { it.isNotBlank() },
        trainerPatronymic?.takeIf { it.isNotBlank() }
    ).joinToString(" ").ifBlank { "тренером" }
}

private fun ClientBookingStatus.toStatusText(): String {
    return when (this) {
        ClientBookingStatus.CREATED -> "\u0417\u0430\u043f\u043b\u0430\u043d\u0438\u0440\u043e\u0432\u0430\u043d\u0430"
        ClientBookingStatus.VISITED -> "\u041f\u043e\u0441\u0435\u0449\u0435\u043d\u043e"
        ClientBookingStatus.NOT_VISITED -> "\u041e\u0442\u0441\u0443\u0442\u0441\u0442\u0432\u043e\u0432\u0430\u043b"
        ClientBookingStatus.CANCELLED -> "\u041e\u0442\u043c\u0435\u043d\u0435\u043d\u0430"
    }
}

private fun formatBookingDateTime(
    date: String,
    startTime: String,
    endTime: String
): String {
    val parsedDate = runCatching { LocalDate.parse(date) }.getOrNull()
    val dateLabel = if (parsedDate != null) {
        val day = parsedDate.dayOfMonth
        val month = parsedDate.month.getDisplayName(java.time.format.TextStyle.FULL, Locale("ru"))
        val weekDay = parsedDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale("ru"))
        "$day $month, $weekDay"
    } else {
        date
    }
    return "$dateLabel \u2022 ${formatBookingTime(startTime)}-${formatBookingTime(endTime)}"
}

private fun formatBookingTime(value: String): String {
    return runCatching {
        LocalDateTime.parse(value).format(DateTimeFormatter.ofPattern("HH:mm"))
    }.getOrElse {
        runCatching {
            OffsetDateTime.parse(value).format(DateTimeFormatter.ofPattern("HH:mm"))
        }.getOrElse {
            value.take(5)
        }
    }
}

private fun formatSlotTimeForBooking(value: String): String {
    return runCatching {
        LocalDateTime.parse(value).format(DateTimeFormatter.ofPattern("HH:mm"))
    }.getOrElse {
        runCatching {
            OffsetDateTime.parse(value).format(DateTimeFormatter.ofPattern("HH:mm"))
        }.getOrElse {
            value.substringAfter('T', value).take(5)
        }
    }
}

private fun formatBookingDateWithWeekday(date: LocalDate): String {
    val locale = Locale("ru")
    val weekDay = date.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, locale)
    val day = date.dayOfMonth
    val month = date.month.getDisplayName(java.time.format.TextStyle.FULL, locale)
    return "$weekDay, $day $month"
}

private fun LocalDate.toEpochMillisForBooking(): Long {
    return atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}

private fun Long.toLocalDateForBooking(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
}

@Composable
internal fun MembershipsStatisticsPage(
    memberships: List<ClientMembership>,
    isLoading: Boolean,
    errorMessage: String?,
    activatingMembershipId: String?,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    onActivateClick: (String) -> Unit
) {
    val hasActiveMembership = memberships.any { it.status == ClientMembershipStatus.ACTIVE }
    var pendingActivationMembershipId by rememberSaveable { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Назад",
                    tint = MainText
                )
            }
            Text(
                text = "Членство",
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
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onRetryClick) {
                        Text("Повторить")
                    }
                }
            }

            memberships.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Абонементы не найдены",
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
                    items(memberships, key = { it.id }) { membership ->
                        MembershipCard(
                            membership = membership,
                            hasActiveMembership = hasActiveMembership,
                            isActivating = activatingMembershipId == membership.id,
                            onActivateClick = { pendingActivationMembershipId = membership.id }
                        )
                    }
                }
            }
        }
    }

    if (pendingActivationMembershipId != null) {
        ActivationConfirmDialog(
            message = "Вы точно хотите активировать абонемент?",
            onConfirm = {
                val id = pendingActivationMembershipId ?: return@ActivationConfirmDialog
                pendingActivationMembershipId = null
                onActivateClick(id)
            },
            onDismiss = { pendingActivationMembershipId = null }
        )
    }
}

@Composable
private fun ActivationConfirmDialog(
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = ScreenBg),
            shape = RoundedCornerShape(2.dp),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color.Black),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = message,
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(AccentDark),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        colors = ButtonDefaults.textButtonColors(contentColor = ScreenBg)
                    ) {
                        Text(
                            text = "Да",
                            fontFamily = FontFamily.Serif,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxSize()
                            .background(ScreenBg.copy(alpha = 0.5f))
                    )
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        colors = ButtonDefaults.textButtonColors(contentColor = ScreenBg)
                    ) {
                        Text(
                            text = "Нет",
                            fontFamily = FontFamily.Serif,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MembershipCard(
    membership: ClientMembership,
    hasActiveMembership: Boolean,
    isActivating: Boolean,
    onActivateClick: () -> Unit
) {
    val statusText = if (membership.status == ClientMembershipStatus.ACTIVE) "Активно" else "Не активно"
    val statusColor = if (membership.status == ClientMembershipStatus.ACTIVE) {
        Color(0xFF2BB34A)
    } else {
        Color(0xFFD4362A)
    }
    val startDate = membership.activatedAt?.toUiDate() ?: "—"
    val endDate = membership.expiresAt?.toUiDate() ?: "—"
    val canActivate = !hasActiveMembership && membership.status == ClientMembershipStatus.PURCHASED

    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Абонемент",
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = membership.membershipTypeName,
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = statusText,
                color = statusColor,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Действует:",
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "С $startDate по $endDate",
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            if (canActivate) {
                Button(
                    onClick = onActivateClick,
                    enabled = !isActivating,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentDark,
                        contentColor = ScreenBg
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp)
                        .size(width = 190.dp, height = 44.dp)
                ) {
                    if (isActivating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = ScreenBg,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Активировать", fontFamily = FontFamily.Serif)
                    }
                }
            }
        }
    }
}

@Composable
internal fun PackagesStatisticsPage(
    packages: List<ClientTrainerPackage>,
    isLoading: Boolean,
    errorMessage: String?,
    activatingPackageId: String?,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    onActivateClick: (String) -> Unit,
    onBookClick: (ClientTrainerPackage) -> Unit
) {
    val hasActivePackage = packages.any { it.status == ClientTrainerPackageStatus.ACTIVE }
    var pendingActivationPackageId by rememberSaveable { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Назад",
                    tint = MainText
                )
            }
            Text(
                text = "Пакет услуг",
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
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onRetryClick) {
                        Text("Повторить")
                    }
                }
            }

            packages.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Пакеты не найдены",
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
                    items(packages, key = { it.id }) { packageItem ->
                        PackageCard(
                            packageItem = packageItem,
                            hasActivePackage = hasActivePackage,
                            isActivating = activatingPackageId == packageItem.id,
                            onActivateClick = { pendingActivationPackageId = packageItem.id },
                            onBookClick = { onBookClick(packageItem) }
                        )
                    }
                }
            }
        }
    }

    if (pendingActivationPackageId != null) {
        ActivationConfirmDialog(
            message = "Вы точно хотите активировать пакет?",
            onConfirm = {
                val id = pendingActivationPackageId ?: return@ActivationConfirmDialog
                pendingActivationPackageId = null
                onActivateClick(id)
            },
            onDismiss = { pendingActivationPackageId = null }
        )
    }
}

@Composable
private fun PackageCard(
    packageItem: ClientTrainerPackage,
    hasActivePackage: Boolean,
    isActivating: Boolean,
    onActivateClick: () -> Unit,
    onBookClick: () -> Unit
) {
    val statusText = if (packageItem.status == ClientTrainerPackageStatus.ACTIVE) "Активно" else "Не активно"
    val statusColor = if (packageItem.status == ClientTrainerPackageStatus.ACTIVE) {
        Color(0xFF2BB34A)
    } else {
        Color(0xFFD4362A)
    }
    val canActivate = !hasActivePackage && packageItem.status == ClientTrainerPackageStatus.PURCHASED
    val canBook = packageItem.status == ClientTrainerPackageStatus.ACTIVE && packageItem.sessionsLeft > 0
    val trainerName = listOfNotNull(
        packageItem.trainerLastName?.takeIf { it.isNotBlank() },
        packageItem.trainerFirstName?.takeIf { it.isNotBlank() },
        packageItem.trainerPatronymic?.takeIf { it.isNotBlank() }
    ).joinToString(" ").ifBlank { null }
    val title = trainerName?.let { "$it ${packageItem.sessionCount} занятий" } ?: packageItem.packageName
    val startDate = (packageItem.activatedAt ?: packageItem.purchasedAt).toUiDate()
    val endDate = packageItem.expiresAt?.toUiDate()

    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = statusText,
                color = statusColor,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleMedium
            )
            if (!endDate.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Действует:",
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = "С $startDate по $endDate",
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
            if (canBook) {
                Text(
                    text = "Осталось услуг: ${packageItem.sessionsLeft}",
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 8.dp)
                )
                Button(
                    onClick = onBookClick,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentDark,
                        contentColor = ScreenBg
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp)
                        .size(width = 190.dp, height = 44.dp)
                ) {
                    Text("Записаться", fontFamily = FontFamily.Serif)
                }
            }
            if (canActivate) {
                Button(
                    onClick = onActivateClick,
                    enabled = !isActivating,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentDark,
                        contentColor = ScreenBg
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp)
                        .size(width = 190.dp, height = 44.dp)
                ) {
                    if (isActivating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = ScreenBg,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Активировать", fontFamily = FontFamily.Serif)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PackageBookingStatisticsPage(
    packageItem: ClientTrainerPackage?,
    selectedDate: LocalDate,
    availableSlots: List<TrainerSlot>,
    selectedSlots: List<PackageBookingSelection>,
    isSlotsLoading: Boolean,
    isSubmitting: Boolean,
    errorMessage: String?,
    infoMessage: String?,
    onBackClick: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onSlotClick: (TrainerSlot) -> Unit,
    onRemoveSelectedClick: (String) -> Unit,
    onRetrySlotsClick: () -> Unit,
    onSubmitClick: () -> Unit
) {
    if (packageItem == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Пакет не выбран.",
                color = MainText,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onBackClick) {
                Text("Назад")
            }
        }
        return
    }

    val trainerName = listOfNotNull(
        packageItem.trainerFirstName?.takeIf { it.isNotBlank() },
        packageItem.trainerLastName?.takeIf { it.isNotBlank() },
        packageItem.trainerPatronymic?.takeIf { it.isNotBlank() }
    ).joinToString(" ").ifBlank { packageItem.packageName }
    val remainingSessions = (packageItem.sessionsLeft - selectedSlots.size).coerceAtLeast(0)

    val selectedDateMillis = selectedDate.toEpochMillisForBooking()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)

    LaunchedEffect(selectedDate) {
        if (datePickerState.selectedDateMillis != selectedDateMillis) {
            datePickerState.selectedDateMillis = selectedDateMillis
        }
    }

    LaunchedEffect(datePickerState) {
        snapshotFlow { datePickerState.selectedDateMillis }
            .filterNotNull()
            .map { millis -> millis.toLocalDateForBooking() }
            .distinctUntilChanged()
            .collect(onDateSelected)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .padding(bottom = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Назад",
                    tint = MainText
                )
            }
            Column {
                Text(
                    text = trainerName,
                    color = MainText,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Осталось ${remainingSessions} занятий",
                    color = MainText.copy(alpha = 0.75f),
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = CardBg),
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

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = formatBookingDateWithWeekday(selectedDate),
            color = MainText,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (isSlotsLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AccentDark)
            }
        } else if (availableSlots.isEmpty()) {
            Text(
                text = "На выбранную дату нет доступных слотов.",
                color = MainText.copy(alpha = 0.75f),
                fontFamily = FontFamily.Serif
            )
        } else {
            val slotsPerRow = 4
            val rows = availableSlots.chunked(slotsPerRow)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                rows.forEach { rowSlots ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowSlots.forEach { slot ->
                            val selected = slot.id?.let { slotId ->
                                selectedSlots.any { selectedSlot -> selectedSlot.slotId == slotId }
                            } ?: false

                            Button(
                                onClick = { onSlotClick(slot) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selected) AccentDark else CardBg,
                                    contentColor = if (selected) ScreenBg else MainText
                                ),
                                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = formatSlotTimeForBooking(slot.startTime),
                                    fontFamily = FontFamily.Serif,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        repeat(slotsPerRow - rowSlots.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        if (!errorMessage.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(6.dp))
            TextButton(onClick = onRetrySlotsClick) {
                Text("Повторить")
            }
        }

        if (!infoMessage.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = infoMessage,
                color = AccentDark,
                fontFamily = FontFamily.Serif
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = CardBg),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Выбранные занятия",
                        color = MainText,
                        fontFamily = FontFamily.Serif,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(
                        onClick = { selectedSlots.forEach { onRemoveSelectedClick(it.slotId) } },
                        colors = ButtonDefaults.textButtonColors(contentColor = AccentDark)
                    ) {
                        Text(
                            text = "Очистить все",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                if (selectedSlots.isEmpty()) {
                    Text(
                        text = "Слоты не выбраны",
                        color = MainText.copy(alpha = 0.72f),
                        fontFamily = FontFamily.Serif
                    )
                } else {
                    selectedSlots.forEach { selectedSlot ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = ScreenBg.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, MainText.copy(alpha = 0.25f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = formatBookingDateWithWeekday(selectedSlot.date),
                                        color = MainText,
                                        fontFamily = FontFamily.Serif,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "${formatSlotTimeForBooking(selectedSlot.startTime)}-${formatSlotTimeForBooking(selectedSlot.endTime)}",
                                        color = MainText,
                                        fontFamily = FontFamily.Serif,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                IconButton(onClick = { onRemoveSelectedClick(selectedSlot.slotId) }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Удалить",
                                        tint = MainText
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Button(
            onClick = onSubmitClick,
            enabled = selectedSlots.isNotEmpty() && !isSubmitting,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentDark,
                contentColor = ScreenBg
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = ScreenBg,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Записаться  ${selectedSlots.size} из ${packageItem.sessionsLeft}",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
internal fun WeightStatisticsPage(
    weightValue: String,
    heightValue: String,
    bmiValue: Double?,
    isSaving: Boolean,
    errorMessage: String?,
    infoMessage: String?,
    onBackClick: () -> Unit,
    onWeightChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onCalculateClick: () -> Unit,
    onWeightDynamicsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Назад",
                    tint = MainText
                )
            }
            Text(
                text = "Вес",
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        WeightInput(
            value = weightValue,
            onValueChange = onWeightChange,
            placeholder = "Текущий вес, кг."
        )
        Spacer(modifier = Modifier.height(12.dp))
        WeightInput(
            value = heightValue,
            onValueChange = onHeightChange,
            placeholder = "Рост, см."
        )

        Spacer(modifier = Modifier.height(22.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = CardBg),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .size(width = 140.dp, height = 44.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = bmiValue?.let { "ИМТ: ${it.toBmiString()}" } ?: "ИМТ",
                    color = MainText.copy(alpha = 0.7f),
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onCalculateClick,
            enabled = !isSaving,
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentDark,
                contentColor = ScreenBg
            ),
            modifier = Modifier.size(width = 190.dp, height = 46.dp)
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = ScreenBg,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Рассчитать",
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Button(
            onClick = onWeightDynamicsClick,
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentDark,
                contentColor = ScreenBg
            ),
            modifier = Modifier.size(width = 190.dp, height = 46.dp)
        ) {
            Text(
                text = "Динамика веса",
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (!errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 14.dp, start = 12.dp, end = 12.dp)
            )
        }
        if (!infoMessage.isNullOrBlank()) {
            Text(
                text = infoMessage,
                color = AccentDark,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp, start = 12.dp, end = 12.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeightInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    TextField(
        value = value,
        onValueChange = { input ->
            val normalized = input.replace(',', '.')
            var dotUsed = false
            val filtered = buildString {
                normalized.forEach { char ->
                    if (char.isDigit()) {
                        append(char)
                    } else if (char == '.' && !dotUsed) {
                        append(char)
                        dotUsed = true
                    }
                }
            }
            onValueChange(filtered)
        },
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
                color = MainText.copy(alpha = 0.42f),
                fontFamily = FontFamily.Serif
            )
        },
        textStyle = MaterialTheme.typography.titleMedium.copy(
            color = MainText,
            fontFamily = FontFamily.Serif
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = CardBg,
            unfocusedContainerColor = CardBg,
            disabledContainerColor = CardBg,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = MainText
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
internal fun WeightDynamicsPage(
    progressHistory: List<ClientProgress>,
    isLoading: Boolean,
    errorMessage: String?,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    val sortedHistory = progressHistory.sortedBy { parseRecordedAtEpoch(it.recordedAt) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Назад",
                    tint = MainText
                )
            }
            Text(
                text = "Динамика веса",
                color = MainText,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AccentDark)
                }
            }

            !errorMessage.isNullOrBlank() -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    TextButton(onClick = onRetryClick) {
                        Text("Повторить")
                    }
                }
            }

            else -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = ScreenBg),
                    shape = RoundedCornerShape(0.dp),
                    border = BorderStroke(1.dp, MainText.copy(alpha = 0.65f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(PaddingValues(horizontal = 10.dp, vertical = 10.dp))
                    ) {
                        WeightChart(
                            progressHistory = sortedHistory,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeightChart(
    progressHistory: List<ClientProgress>,
    modifier: Modifier = Modifier
) {
    if (progressHistory.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                text = "Нет данных для графика",
                color = MainText,
                fontFamily = FontFamily.Serif
            )
        }
        return
    }

    val sorted = progressHistory.sortedBy { parseRecordedAtEpoch(it.recordedAt) }
    val minWeight = sorted.minOf { it.weight }
    val maxWeight = sorted.maxOf { it.weight }
    val weightRange = (maxWeight - minWeight).takeIf { it > 0.0 } ?: 1.0

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val leftPadding = 18.dp.toPx()
            val rightPadding = 10.dp.toPx()
            val topPadding = 14.dp.toPx()
            val bottomPadding = 18.dp.toPx()
            val chartWidth = size.width - leftPadding - rightPadding
            val chartHeight = size.height - topPadding - bottomPadding

            drawLine(
                color = MainText.copy(alpha = 0.25f),
                start = Offset(leftPadding, topPadding),
                end = Offset(leftPadding, topPadding + chartHeight),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = MainText.copy(alpha = 0.25f),
                start = Offset(leftPadding, topPadding + chartHeight),
                end = Offset(leftPadding + chartWidth, topPadding + chartHeight),
                strokeWidth = 1.dp.toPx()
            )

            val path = Path()
            val points = sorted.mapIndexed { index, item ->
                val x = if (sorted.size == 1) {
                    leftPadding + chartWidth / 2f
                } else {
                    leftPadding + chartWidth * (index.toFloat() / (sorted.lastIndex.toFloat()))
                }
                val normalized = ((item.weight - minWeight) / weightRange).toFloat()
                val y = topPadding + chartHeight * (1f - normalized)
                Offset(x, y)
            }

            points.forEachIndexed { index, point ->
                if (index == 0) {
                    path.moveTo(point.x, point.y)
                } else {
                    path.lineTo(point.x, point.y)
                }
            }

            drawPath(
                path = path,
                color = AccentDark,
                style = Stroke(width = 2.5.dp.toPx())
            )
            points.forEach { point ->
                drawCircle(
                    color = AccentDark,
                    radius = 4.dp.toPx(),
                    center = point
                )
                drawCircle(
                    color = ScreenBg,
                    radius = 2.dp.toPx(),
                    center = point
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "${minWeight.toBmiString()} кг",
                color = MainText.copy(alpha = 0.8f),
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${maxWeight.toBmiString()} кг",
                color = MainText.copy(alpha = 0.8f),
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = sorted.first().recordedAt.toUiDateTime(),
                color = MainText.copy(alpha = 0.7f),
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = sorted.last().recordedAt.toUiDateTime(),
                color = MainText.copy(alpha = 0.7f),
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun parseRecordedAtEpoch(value: String): Long {
    return runCatching { Instant.parse(value).toEpochMilli() }
        .getOrElse {
            runCatching { OffsetDateTime.parse(value).toInstant().toEpochMilli() }
                .getOrElse { Long.MAX_VALUE }
        }
}

private fun Double.toBmiString(): String = String.format(Locale.US, "%.2f", this)

private fun String.toUiDateTime(): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")
    return runCatching { Instant.parse(this).atZone(java.time.ZoneId.systemDefault()).toLocalDate().format(formatter) }
        .getOrElse {
            runCatching { OffsetDateTime.parse(this).toLocalDate().format(formatter) }
                .getOrElse { this }
        }
}

private fun String.toUiDate(): String {
    return runCatching {
        LocalDate.parse(this).format(DateTimeFormatter.ofPattern("dd.MM.yy", Locale.forLanguageTag("ru")))
    }.getOrElse { this }
}
