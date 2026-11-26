package com.example.ui.screens

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.SampleData
import com.example.ui.QuizUiState
import com.example.ui.components.MiaAvatarBadge
import com.example.ui.theme.MiaGoldContainer
import com.example.ui.theme.MiaMint
import com.example.ui.theme.MiaMintContainer
import com.example.ui.theme.MiaPink
import com.example.ui.theme.MiaPinkContainer
import com.example.ui.theme.MiaPurple
import com.example.ui.theme.MiaPurpleContainer

@Composable
fun QuizScreen(
    quizState: QuizUiState,
    onSelectCategory: (String) -> Unit,
    onSelectOption: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    onRestartQuiz: () -> Unit,
    onGoToChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All Mix", "Greetings", "Slang", "Music", "School")

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "⚡ Mia Quiz",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "+${SampleData.XP_PER_CORRECT_QUIZ} XP for every right answer!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(
                        color = MiaGoldContainer,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "✨ +${quizState.sessionXpEarned} XP",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF4F3400),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = quizState.selectedCategory == cat,
                            onClick = { onSelectCategory(cat) },
                            label = {
                                Text(
                                    text = cat,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MiaPink,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("quiz_category_$cat")
                        )
                    }
                }
            }

            if (quizState.isFinished) {
                item {
                    QuizCompletedCard(
                        score = quizState.sessionScore,
                        total = quizState.questions.size,
                        xpEarned = quizState.sessionXpEarned,
                        onRestartQuiz = onRestartQuiz,
                        onGoToChat = onGoToChat
                    )
                }
            } else {
                val question = quizState.questions.getOrNull(quizState.currentIndex)
                if (question != null) {
                    val progress = (quizState.currentIndex + 1).toFloat() / quizState.questions.size.toFloat()

                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Question ${quizState.currentIndex + 1} of ${quizState.questions.size}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MiaPurple
                                )
                                Text(
                                    text = "${question.emoji} ${question.category}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MiaPink
                                )
                            }
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(50)),
                                color = MiaPurple,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                strokeCap = StrokeCap.Round
                            )
                        }
                    }

                    item {
                        Card(
                            shape = RoundedCornerShape(26.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.5.dp, MiaPurple.copy(alpha = 0.25f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    color = MiaPurpleContainer,
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = "💡 ${question.contextHint}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color(0xFF280E6B),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                                Text(
                                    text = question.question,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    items(question.options.indices.toList()) { idx ->
                        val optionText = question.options[idx]
                        val isSelected = quizState.selectedOptionIndex == idx
                        val isCorrect = idx == question.correctIndex

                        val containerColor = when {
                            !quizState.isAnswered -> MaterialTheme.colorScheme.surface
                            isCorrect -> MiaMintContainer
                            isSelected && !isCorrect -> MiaPinkContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        }
                        val borderColor = when {
                            !quizState.isAnswered -> MaterialTheme.colorScheme.outline
                            isCorrect -> MiaMint
                            isSelected && !isCorrect -> MiaPink
                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .clickable(enabled = !quizState.isAnswered) {
                                    onSelectOption(idx)
                                }
                                .testTag("quiz_option_$idx"),
                            shape = RoundedCornerShape(20.dp),
                            color = containerColor,
                            border = BorderStroke(1.5.dp, borderColor)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp, vertical = 18.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                val letter = ('A' + idx).toString()
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                quizState.isAnswered && isCorrect -> MiaMint
                                                quizState.isAnswered && isSelected && !isCorrect -> MiaPink
                                                else -> MiaPurpleContainer
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = letter,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = if (quizState.isAnswered && (isCorrect || isSelected)) Color.White
                                        else Color(0xFF280E6B)
                                    )
                                }

                                Text(
                                    text = optionText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )

                                if (quizState.isAnswered && isCorrect) {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = "Correct",
                                        tint = MiaMint
                                    )
                                } else if (quizState.isAnswered && isSelected && !isCorrect) {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = "Incorrect",
                                        tint = MiaPink
                                    )
                                }
                            }
                        }
                    }

                    if (quizState.isAnswered) {
                        val gotItRight = quizState.selectedOptionIndex == question.correctIndex
                        item {
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                color = if (gotItRight) MiaMintContainer else MiaPurpleContainer,
                                border = BorderStroke(
                                    1.dp,
                                    if (gotItRight) MiaMint.copy(alpha = 0.5f) else MiaPurple.copy(alpha = 0.4f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        MiaAvatarBadge(size = 46.dp, showOnlineDot = false)
                                        Column {
                                            Text(
                                                text = if (gotItRight) "Slay! +10 XP! 🎉🔥"
                                                else "Quase! Keep going! 💪",
                                                style = MaterialTheme.typography.titleLarge,
                                                color = Color(0xFF1A1429)
                                            )
                                            Text(
                                                text = question.explanationEn,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color(0xFF2D2442)
                                            )
                                        }
                                    }

                                    Surface(
                                        color = Color.White.copy(alpha = 0.75f),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "🇦🇴 PT: ${question.explanationPt}",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF280E6B),
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }

                                    Button(
                                        onClick = onNextQuestion,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(54.dp)
                                            .testTag("quiz_next_button"),
                                        shape = RoundedCornerShape(18.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MiaPurple)
                                    ) {
                                        val isLast = quizState.currentIndex + 1 >= quizState.questions.size
                                        Text(
                                            text = if (isLast) "See My Score! 🏆" else "Next Question →",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizCompletedCard(
    score: Int,
    total: Int,
    xpEarned: Int,
    onRestartQuiz: () -> Unit,
    onGoToChat: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(Brush.linearGradient(listOf(MiaPurple, MiaPink)))
    ) {
        Column(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(MiaPurple, MiaPink)))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            MiaAvatarBadge(size = 84.dp, showOnlineDot = false)
            Text(
                text = "Quiz Complete! 🏆🎉",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = "You scored $score out of $total and earned +$xpEarned XP!",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.95f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = onRestartQuiz,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("quiz_restart_button"),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    text = "Play Again 🔄",
                    style = MaterialTheme.typography.titleMedium,
                    color = MiaPurple
                )
            }
            OutlinedButton(
                onClick = onGoToChat,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("quiz_go_chat_button"),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.5.dp, Color.White)
            ) {
                Text(
                    text = "Chat with Mia 💬",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}
