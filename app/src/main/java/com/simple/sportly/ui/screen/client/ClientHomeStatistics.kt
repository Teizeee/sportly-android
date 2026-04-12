package com.simple.sportly.ui.screen.client

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.simple.sportly.domain.model.ClientMembership
import com.simple.sportly.domain.model.ClientMembershipStatus
import com.simple.sportly.domain.model.ClientProgress
import com.simple.sportly.domain.model.ClientTrainerPackage
import com.simple.sportly.domain.model.ClientTrainerPackageStatus
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
internal fun StatisticsTab(
    activeMembership: ActiveMembership?,
    activePackage: ActivePackage?,
    isLoading: Boolean,
    errorMessage: String?,
    onRetryClick: () -> Unit,
    onMembershipsClick: () -> Unit,
    onPackagesClick: () -> Unit,
    onWeightClick: () -> Unit
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
            subtitle = null
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
    val startDate = (membership.activatedAt ?: membership.purchasedAt).toUiDate()
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
    onActivateClick: (String) -> Unit
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
                            onActivateClick = { pendingActivationPackageId = packageItem.id }
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
    onActivateClick: () -> Unit
) {
    val statusText = if (packageItem.status == ClientTrainerPackageStatus.ACTIVE) "Активно" else "Не активно"
    val statusColor = if (packageItem.status == ClientTrainerPackageStatus.ACTIVE) {
        Color(0xFF2BB34A)
    } else {
        Color(0xFFD4362A)
    }
    val canActivate = !hasActivePackage && packageItem.status == ClientTrainerPackageStatus.PURCHASED
    val canBook = packageItem.status == ClientTrainerPackageStatus.ACTIVE
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
                    onClick = {},
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

