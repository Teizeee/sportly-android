package com.simple.sportly.ui.screen.trainer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simple.sportly.domain.model.TrainerReview
import java.util.Locale

@Composable
internal fun ReviewsTab(
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

