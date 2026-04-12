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

internal val ScreenBg = Color(0xFFE7E3DA)
internal val CardBg = Color(0xFFD1CAB9)
internal val AccentDark = Color(0xFF565347)
internal val MainText = Color(0xFF29251F)
internal val OpenButtonColor = Color(0xFF5B8A5A)
internal val CloseButtonColor = Color(0xFFB6544A)

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