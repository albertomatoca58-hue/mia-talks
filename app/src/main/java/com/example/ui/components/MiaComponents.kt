package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.UserStatsEntity
import com.example.ui.DayActivityBar
import com.example.ui.theme.MiaGold
import com.example.ui.theme.MiaGoldContainer
import com.example.ui.theme.MiaMint
import com.example.ui.theme.MiaPink
import com.example.ui.theme.MiaPinkContainer
import com.example.ui.theme.MiaPurple
import com.example.ui.theme.MiaPurpleContainer

@Composable
fun MiaAvatarBadge(
    size: Dp = 72.dp,
    showOnlineDot: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(size)) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(MiaPurple, MiaPink)
                    )
                )
                .padding(3.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_mia_avatar),
                contentDescription = "Mia, your English tutor with curly hair",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size - 6.dp)
                    .clip(CircleShape)
            )
        }
        if (showOnlineDot) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(size * 0.25f)
                    .clip(CircleShape)
                    .background(MiaMint)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
            )
        }
    }
}

@Composable
fun XpAndStreakPills(
    totalXp: Int,
    currentStreak: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = MiaGoldContainer,
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, MiaGold.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "✨", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$totalXp XP",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF4F3400)
                )
            }
        }

        Surface(
            color = MiaPinkContainer,
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, MiaPink.copy(alpha = 0.35f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🔥", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$currentStreak day streak",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF6B002B)
                )
            }
        }
    }
}

@Composable
fun ProgressDashboardCard(
    stats: UserStatsEntity,
    completedLessons: Int,
    totalLessons: Int,
    weeklyActivity: List<DayActivityBar>,
    modifier: Modifier = Modifier
) {
    val level = (stats.totalXp / 100) + 1
    val xpIntoLevel = stats.totalXp % 100
    val levelTitle = when (level) {
        1 -> "Luanda Starter 🌟"
        2 -> "Vibe Learner 🔥"
        3 -> "Slang Explorer 😎"
        4 -> "English Pro 🚀"
        else -> "Legend Status 👑"
    }
    val maxDayXp = (weeklyActivity.maxOfOrNull { it.xpEarned } ?: 0).coerceAtLeast(30)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "📊 Your Progress Dashboard",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Level $level • $levelTitle",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MiaPurple,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Surface(
                    color = MiaPurpleContainer,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "$xpIntoLevel / 100 XP",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF391487),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            LinearProgressIndicator(
                progress = { xpIntoLevel / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50)),
                color = MiaPink,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DashboardMetricTile(
                    emoji = "⚡",
                    value = "${stats.totalXp}",
                    label = "Total XP",
                    containerColor = MiaPurpleContainer,
                    contentColor = Color(0xFF2D0D73),
                    modifier = Modifier.weight(1f)
                )
                DashboardMetricTile(
                    emoji = "🔥",
                    value = "${stats.currentStreakDays}d",
                    label = "Best: ${stats.bestStreakDays}d",
                    containerColor = MiaPinkContainer,
                    contentColor = Color(0xFF6B002B),
                    modifier = Modifier.weight(1f)
                )
                DashboardMetricTile(
                    emoji = "✅",
                    value = "$completedLessons/$totalLessons",
                    label = "Lessons",
                    containerColor = MiaGoldContainer,
                    contentColor = Color(0xFF4F3400),
                    modifier = Modifier.weight(1f)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Last 7 Days Activity",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(92.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    weeklyActivity.forEach { day ->
                        val fraction = (day.xpEarned.toFloat() / maxDayXp.toFloat()).coerceIn(0f, 1f)
                        val barHeight = (10f + fraction * 48f).dp
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.weight(1f)
                        ) {
                            if (day.xpEarned > 0) {
                                Text(
                                    text = "${day.xpEarned}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (day.isToday) MiaPink else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Spacer(modifier = Modifier.height(14.dp))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(barHeight)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
                                    .background(
                                        when {
                                            day.xpEarned == 0 -> androidx.compose.ui.graphics.SolidColor(
                                                MaterialTheme.colorScheme.surfaceVariant
                                            )
                                            day.isToday -> Brush.verticalGradient(listOf(MiaPink, MiaPurple))
                                            else -> Brush.verticalGradient(listOf(MiaPurple, MiaPurple.copy(alpha = 0.7f)))
                                        }
                                    )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = day.dayLabel,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Medium,
                                color = if (day.isToday) MiaPurple else MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardMetricTile(
    emoji: String,
    value: String,
    label: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = containerColor
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = contentColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor.copy(alpha = 0.85f),
                maxLines = 1
            )
        }
    }
}
