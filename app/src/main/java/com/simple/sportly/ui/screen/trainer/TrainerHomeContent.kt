package com.simple.sportly.ui.screen.trainer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScheduleTab(
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

