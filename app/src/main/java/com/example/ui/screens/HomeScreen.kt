package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.SampleData
import com.example.data.UserStatsEntity
import com.example.ui.DayActivityBar
import com.example.ui.components.MiaAvatarBadge
import com.example.ui.components.ProgressDashboardCard
import com.example.ui.components.XpAndStreakPills
import com.example.ui.theme.MiaPink
import com.example.ui.theme.MiaPinkContainer
import com.example.ui.theme.MiaPurple
import com.example.ui.theme.MiaPurpleContainer

@Composable
fun HomeScreen(
    stats: UserStatsEntity,
    completedLessonsCount: Int,
    weeklyActivity: List<DayActivityBar>,
    onChatClick: () -> Unit,
    onLessonsClick: () -> Unit,
    onQuizClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 640.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Hero card with Mia avatar, curly hair portrait, greeting, XP & Streak
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(30.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(MiaPurple, Color(0xFF9C4DFF), MiaPink)
                            )
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_home_banner),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                            .alpha(0.24f)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            MiaAvatarBadge(size = 86.dp, showOnlineDot = true)
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Surface(
                                    color = Color.White.copy(alpha = 0.22f),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = "🇦🇴 Angola Teen English",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                                Text(
                                    text = stringResource(R.string.home_greeting),
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = Color.White
                                )
                                Text(
                                    text = "Your 16yo English bestie! Let's learn slang, chat & level up! ✨",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.94f)
                                )
                            }
                        }

                        XpAndStreakPills(
                            totalXp = stats.totalXp,
                            currentStreak = stats.currentStreakDays
                        )
                    }
                }
            }

            item {
                Text(
                    text = "What's the vibe today? 🚀",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Big Button 1: Chat with Mia
            item {
                BigActionCard(
                    emoji = "💬",
                    title = "Chat with Mia",
                    subtitle = "WhatsApp-style chat • Live AI & offline sample chats",
                    badgeText = "Popular",
                    gradient = Brush.horizontalGradient(listOf(MiaPurple, MiaPink)),
                    contentColor = Color.White,
                    borderColor = null,
                    onClick = onChatClick,
                    modifier = Modifier.testTag("btn_chat_with_mia")
                )
            }

            // Big Button 2: Lessons
            item {
                BigActionCard(
                    emoji = "📚",
                    title = "Lessons",
                    subtitle = "Greetings, Slang, Music, School & Hangouts",
                    badgeText = "$completedLessonsCount/${SampleData.lessons.size} done",
                    gradient = Brush.horizontalGradient(
                        listOf(MiaPurpleContainer, MiaPurpleContainer)
                    ),
                    contentColor = Color(0xFF280E6B),
                    borderColor = MiaPurple.copy(alpha = 0.25f),
                    onClick = onLessonsClick,
                    modifier = Modifier.testTag("btn_lessons")
                )
            }

            // Big Button 3: Quiz
            item {
                BigActionCard(
                    emoji = "⚡",
                    title = "Quiz",
                    subtitle = "Quick challenges • +10 XP for each right answer!",
                    badgeText = "+10 XP",
                    gradient = Brush.horizontalGradient(
                        listOf(MiaPinkContainer, MiaPinkContainer)
                    ),
                    contentColor = Color(0xFF6B002B),
                    borderColor = MiaPink.copy(alpha = 0.28f),
                    onClick = onQuizClick,
                    modifier = Modifier.testTag("btn_quiz")
                )
            }

            // Progress Dashboard
            item {
                ProgressDashboardCard(
                    stats = stats,
                    completedLessons = completedLessonsCount,
                    totalLessons = SampleData.lessons.size,
                    weeklyActivity = weeklyActivity,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun BigActionCard(
    emoji: String,
    title: String,
    subtitle: String,
    badgeText: String,
    gradient: Brush,
    contentColor: Color,
    borderColor: Color?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(26.dp),
        border = borderColor?.let { BorderStroke(1.5.dp, it) },
        shadowElevation = if (borderColor == null) 6.dp else 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient)
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(
                        if (borderColor == null) Color.White.copy(alpha = 0.22f)
                        else Color.White.copy(alpha = 0.82f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = contentColor,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        color = if (borderColor == null) Color.White.copy(alpha = 0.24f)
                        else contentColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall,
                            color = contentColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.88f)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = title,
                tint = contentColor,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}
