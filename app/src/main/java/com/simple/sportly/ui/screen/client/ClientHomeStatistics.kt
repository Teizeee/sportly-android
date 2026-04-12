package com.simple.sportly.ui.screen.client

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.simple.sportly.domain.model.ActiveMembership
import com.simple.sportly.domain.model.ActivePackage
import com.simple.sportly.domain.model.ClientMembership
import com.simple.sportly.domain.model.ClientMembershipStatus
import com.simple.sportly.domain.model.ClientTrainerPackage
import com.simple.sportly.domain.model.ClientTrainerPackageStatus
import java.time.LocalDate
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
    onPackagesClick: () -> Unit
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
            subtitle = "Калькулятор ИМТ"
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
                    style = MaterialTheme.typography.titleLarge
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

private fun String.toUiDate(): String {
    return runCatching {
        LocalDate.parse(this).format(DateTimeFormatter.ofPattern("dd.MM.yy", Locale.forLanguageTag("ru")))
    }.getOrElse { this }
}

