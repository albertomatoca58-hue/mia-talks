package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.LessonItem
import com.example.data.SampleData
import com.example.ui.components.MiaAvatarBadge
import com.example.ui.theme.MiaGoldContainer
import com.example.ui.theme.MiaMint
import com.example.ui.theme.MiaMintContainer
import com.example.ui.theme.MiaPink
import com.example.ui.theme.MiaPinkContainer
import com.example.ui.theme.MiaPurple
import com.example.ui.theme.MiaPurpleContainer

@Composable
fun LessonsScreen(
    selectedLessonId: String?,
    completedLessonIds: Set<String>,
    onSelectLesson: (String?) -> Unit,
    onCompleteLesson: (LessonItem) -> Unit,
    onPracticeWithMia: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeLesson = selectedLessonId?.let { id ->
        SampleData.lessons.firstOrNull { it.id == id }
    }

    if (activeLesson != null) {
        BackHandler { onSelectLesson(null) }
        LessonDetailView(
            lesson = activeLesson,
            isCompleted = completedLessonIds.contains(activeLesson.id),
            onBack = { onSelectLesson(null) },
            onCompleteLesson = { onCompleteLesson(activeLesson) },
            onPracticeWithMia = {
                val threadId = when (activeLesson.id) {
                    "greetings" -> SampleData.THREAD_GREETINGS
                    "slang" -> SampleData.THREAD_SLANG
                    "music" -> SampleData.THREAD_MUSIC
                    "school" -> SampleData.THREAD_SCHOOL
                    else -> SampleData.THREAD_LIVE_AI
                }
                onPracticeWithMia(threadId)
            },
            modifier = modifier
        )
    } else {
        LessonsListView(
            lessons = SampleData.lessons,
            completedLessonIds = completedLessonIds,
            onSelectLesson = { onSelectLesson(it.id) },
            modifier = modifier
        )
    }
}

@Composable
private fun LessonsListView(
    lessons: List<LessonItem>,
    completedLessonIds: Set<String>,
    onSelectLesson: (LessonItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val completedCount = lessons.count { completedLessonIds.contains(it.id) }
    val progress = if (lessons.isEmpty()) 0f else completedCount.toFloat() / lessons.size.toFloat()

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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "📚 Mia's Lessons",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Pick a topic, learn real teen English, and earn +25 XP!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = MiaPurpleContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Course Progress 🔥",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF280E6B)
                            )
                            Text(
                                text = "$completedCount / ${lessons.size} completed",
                                style = MaterialTheme.typography.labelLarge,
                                color = MiaPurple
                            )
                        }
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(50)),
                            color = MiaPink,
                            trackColor = Color.White,
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
            }

            items(lessons, key = { it.id }) { lesson ->
                val isDone = completedLessonIds.contains(lesson.id)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { onSelectLesson(lesson) }
                        .testTag("lesson_card_${lesson.id}"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(
                        1.5.dp,
                        if (isDone) MiaMint.copy(alpha = 0.6f)
                        else MaterialTheme.colorScheme.outline
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(
                                    if (isDone) MiaMintContainer else MiaPinkContainer
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = lesson.emoji,
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
                                    text = lesson.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (isDone) {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = "Completed",
                                        tint = MiaMint,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Text(
                                text = lesson.tagline,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Surface(
                                    color = MiaGoldContainer,
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = if (isDone) "Completed ✓" else "+${lesson.xpReward} XP",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF4F3400),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = "${lesson.difficulty} • ${lesson.durationMinutes} min",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = "Open ${lesson.title}",
                            tint = MiaPurple
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonDetailView(
    lesson: LessonItem,
    isCompleted: Boolean,
    onBack: () -> Unit,
    onCompleteLesson: () -> Unit,
    onPracticeWithMia: () -> Unit,
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
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .testTag("btn_lesson_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back to lessons",
                            tint = MiaPurple
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${lesson.emoji} ${lesson.title}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = lesson.tagline,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MiaPurpleContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MiaAvatarBadge(size = 54.dp, showOnlineDot = false)
                        Text(
                            text = lesson.miaIntro,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF280E6B),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Key Phrases 🗣️",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(lesson.phrases) { phrase ->
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = phrase.english,
                            style = MaterialTheme.typography.titleLarge,
                            color = MiaPurple
                        )
                        Text(
                            text = "🇦🇴 ${phrase.portuguese}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "🔊 Say it like: \"${phrase.pronunciationHint}\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = MiaPink,
                            fontWeight = FontWeight.Medium
                        )
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "💬 Example: \"${phrase.exampleUsage}\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }

            item {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = MiaPinkContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Mini WhatsApp Practice 💬",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF6B002B)
                        )
                        Text(
                            text = "You: \"${lesson.miniDialogueUser}\"",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A001E)
                        )
                        Text(
                            text = "Mia: \"${lesson.miniDialogueMia}\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF4A001E)
                        )
                    }
                }
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Button(
                        onClick = onCompleteLesson,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .testTag("complete_lesson_button"),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCompleted) MiaMint else MiaPurple
                        )
                    ) {
                        Text(
                            text = if (isCompleted) "Lesson Completed! ✅ (Review Again)"
                            else "Complete Lesson (+${lesson.xpReward} XP) 🎉",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }

                    OutlinedButton(
                        onClick = onPracticeWithMia,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("practice_lesson_chat_button"),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.5.dp, MiaPink)
                    ) {
                        Text(
                            text = "Practice this in Chat with Mia 💬",
                            style = MaterialTheme.typography.titleMedium,
                            color = MiaPink
                        )
                    }
                }
            }
        }
    }
}
