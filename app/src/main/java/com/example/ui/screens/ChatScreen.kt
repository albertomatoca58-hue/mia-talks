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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.data.ChatMessageEntity
import com.example.data.OfflinePracticeReply
import com.example.data.SampleData
import com.example.ui.AiChatUiState
import com.example.ui.components.MiaAvatarBadge
import com.example.ui.theme.MiaChatBgDark
import com.example.ui.theme.MiaChatBgLight
import com.example.ui.theme.MiaMint
import com.example.ui.theme.MiaPink
import com.example.ui.theme.MiaPinkContainer
import com.example.ui.theme.MiaPurple
import com.example.ui.theme.MiaPurpleContainer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    activeThreadId: String,
    messages: List<ChatMessageEntity>,
    aiState: AiChatUiState,
    usedPracticeReplyIds: Set<String>,
    isDarkTheme: Boolean,
    isOnline: Boolean,
    onSelectThread: (String) -> Unit,
    onSendLiveMessage: (String) -> Unit,
    onRetryLiveMessage: () -> Unit,
    onSendOfflinePracticeReply: (OfflinePracticeReply) -> Unit,
    onClearLiveChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()
    val currentThread = SampleData.chatThreads.firstOrNull { it.id == activeThreadId }
        ?: SampleData.chatThreads.first()
    val isLiveMode = currentThread.isLiveAi
    val practiceReplies = remember(activeThreadId) {
        SampleData.offlinePracticeReplies.filter { it.threadId == activeThreadId }
    }

    LaunchedEffect(messages.size, aiState) {
        val targetIndex = (messages.size - 1).coerceAtLeast(0)
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(targetIndex)
        }
    }

    val chatCanvasColor = if (isDarkTheme) MiaChatBgDark else MiaChatBgLight

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(chatCanvasColor)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // WhatsApp-style Mia Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 3.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MiaAvatarBadge(size = 48.dp, showOnlineDot = !isLiveMode || isOnline)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mia ✨",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        val statusText = when {
                            aiState is AiChatUiState.Streaming -> "typing..."
                            !isLiveMode -> "Offline Sample Chat • Ready anytime 📶"
                            isOnline -> "Online • 16yo English Bestie 🇦🇴"
                            else -> "Offline • Switch to Sample Chats below"
                        }
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (aiState is AiChatUiState.Streaming) MiaPink
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (isLiveMode) {
                        IconButton(
                            onClick = onClearLiveChat,
                            modifier = Modifier.testTag("btn_reset_chat")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Refresh,
                                contentDescription = "Reset chat",
                                tint = MiaPurple
                            )
                        }
                    }
                }

                // Thread switcher: Live AI + Offline Sample Chats
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(SampleData.chatThreads, key = { it.id }) { thread ->
                        val selected = thread.id == activeThreadId
                        FilterChip(
                            selected = selected,
                            onClick = { onSelectThread(thread.id) },
                            label = {
                                Text(
                                    text = "${thread.emoji} ${thread.title}",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MiaPurple,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("thread_chip_${thread.id}")
                        )
                    }
                }
            }
        }

        // Messages list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .widthIn(max = 640.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (!isLiveMode) {
                item {
                    Surface(
                        color = MiaPurpleContainer,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "📶 Offline Sample Chat: ${currentThread.emoji} ${currentThread.title} — works with zero internet! Tap the practice replies below to earn +5 XP.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF2D0D73),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                }
            }

            items(messages, key = { it.id }) { msg ->
                WhatsAppBubble(message = msg)
            }

            if (isLiveMode && aiState is AiChatUiState.Streaming) {
                item {
                    WhatsAppBubble(
                        message = ChatMessageEntity(
                            threadId = activeThreadId,
                            sender = "mia",
                            text = aiState.partialText.ifBlank { "Thinking of a cool reply... ✨" }
                        ),
                        isStreaming = true
                    )
                }
            }

            if (isLiveMode && aiState is AiChatUiState.Error) {
                item {
                    Surface(
                        color = MiaPinkContainer,
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, MiaPink.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.WifiOff,
                                    contentDescription = "Connection notice",
                                    tint = Color(0xFF8C0032)
                                )
                                Text(
                                    text = aiState.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF5E0028)
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = onRetryLiveMessage,
                                    colors = ButtonDefaults.buttonColors(containerColor = MiaPurple),
                                    modifier = Modifier.testTag("btn_retry_ai")
                                ) {
                                    Text("Retry 🔄")
                                }
                                OutlinedButton(
                                    onClick = { onSelectThread(SampleData.THREAD_SLANG) },
                                    modifier = Modifier.testTag("btn_switch_sample_chat")
                                ) {
                                    Text("Open Sample Chats 🔥")
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom input or offline practice bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 640.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isLiveMode) {
                    val starterChips = listOf(
                        "Teach me a cool slang! 🔥",
                        "Yesterday I go to Luanda beach",
                        "How do I say 'tá fixe' in English?"
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(starterChips) { starter ->
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(50),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .clickable(enabled = aiState !is AiChatUiState.Streaming) {
                                        onSendLiveMessage(starter)
                                    }
                            ) {
                                Text(
                                    text = starter,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = {
                                Text("Message Mia in English (ou PT)...")
                            },
                            singleLine = false,
                            maxLines = 3,
                            shape = RoundedCornerShape(26.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    if (inputText.isNotBlank() && aiState !is AiChatUiState.Streaming) {
                                        onSendLiveMessage(inputText)
                                        inputText = ""
                                    }
                                }
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MiaPurple,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("chat_input_field")
                        )

                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(listOf(MiaPurple, MiaPink))
                                )
                                .clickable(
                                    enabled = inputText.isNotBlank() && aiState !is AiChatUiState.Streaming
                                ) {
                                    onSendLiveMessage(inputText)
                                    inputText = ""
                                }
                                .testTag("chat_send_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.Send,
                                contentDescription = "Send message",
                                tint = Color.White
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Tap a reply to practice offline with Mia (+5 XP):",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        practiceReplies.forEach { reply ->
                            val alreadyTried = usedPracticeReplyIds.contains(reply.id)
                            Surface(
                                color = if (alreadyTried) MaterialTheme.colorScheme.surfaceVariant
                                else MiaPurpleContainer,
                                shape = RoundedCornerShape(18.dp),
                                border = BorderStroke(
                                    1.dp,
                                    if (alreadyTried) MaterialTheme.colorScheme.outline
                                    else MiaPurple.copy(alpha = 0.4f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(18.dp))
                                    .clickable { onSendOfflinePracticeReply(reply) }
                                    .testTag("offline_reply_${reply.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "💬 \"${reply.userPrompt}\"",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (alreadyTried) "✓" else "+5 XP",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (alreadyTried) MiaMint else MiaPink
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

@Composable
private fun WhatsAppBubble(
    message: ChatMessageEntity,
    isStreaming: Boolean = false
) {
    val isUser = message.sender == "user"
    val timeStr = remember(message.timestampMillis) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestampMillis))
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            MiaAvatarBadge(
                size = 34.dp,
                showOnlineDot = false,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(end = 8.dp)
            )
        }

        val bubbleShape = if (isUser) {
            RoundedCornerShape(topStart = 20.dp, topEnd = 4.dp, bottomStart = 20.dp, bottomEnd = 20.dp)
        } else {
            RoundedCornerShape(topStart = 4.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 20.dp)
        }

        Surface(
            shape = bubbleShape,
            color = if (isUser) Color.Transparent else MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp,
            border = if (isUser) null else BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
            ),
            modifier = Modifier.widthIn(max = 310.dp)
        ) {
            Box(
                modifier = if (isUser) {
                    Modifier.background(
                        Brush.linearGradient(listOf(MiaPurple, MiaPink))
                    )
                } else {
                    Modifier
                }
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (!isUser) {
                        Text(
                            text = if (isStreaming) "Mia ✨ typing..." else "Mia",
                            style = MaterialTheme.typography.labelSmall,
                            color = MiaPink,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
                    )

                    if (!message.grammarTip.isNullOrBlank()) {
                        Surface(
                            color = MiaPinkContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "✏️ Fix tip: ${message.grammarTip}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF6B002B),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    if (!message.portugueseNote.isNullOrBlank()) {
                        Surface(
                            color = MiaPurpleContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "🇦🇴 PT: ${message.portugueseNote}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF280E6B),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.align(Alignment.End),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = timeStr,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isUser) Color.White.copy(alpha = 0.8f)
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (isUser) {
                            Text(
                                text = "✓✓",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFB9F6CA)
                            )
                        }
                    }
                }
            }
        }
    }
}
