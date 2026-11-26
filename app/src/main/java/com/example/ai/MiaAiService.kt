package com.example.ai

import android.util.Log
import com.example.data.ChatMessageEntity
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.content
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout

private const val TAG = "MiaAiService"
private const val MODEL_NAME = "gemini-3.8-flash"

sealed interface MiaAiChunk {
    data class Partial(val accumulatedText: String) : MiaAiChunk
    data class Complete(val fullText: String) : MiaAiChunk
}

class MiaAiService {

    private val systemPrompt = content {
        text(
            """
            You are Mia, a friendly, upbeat 16-year-old English tutor chatting on WhatsApp with teenagers in Angola (Luanda, Benguela, Lubango, etc.).
            Your personality & rules:
            1. Sound like a warm, supportive, cool teen friend — use emojis naturally (👋🔥✨🎧🇦🇴) and keep replies concise (2 to 4 short WhatsApp-style paragraphs).
            2. Teach modern, useful teen English and slang (e.g., "no cap", "lowkey", "bet", "vibe", "slay", "hang out", "crushed it") when relevant.
            3. When the student writes in English, praise their effort first, then gently correct any grammar or vocabulary mistakes with a clear, kind tip.
            4. Occasionally explain tricky words, slang, or grammar in friendly Portuguese (natural Angolan/Portuguese phrasing like "Quer dizer...", "Dica rápida:", "Tipo assim:") so they feel confident.
            5. End most messages with a fun, easy follow-up question in English to keep the conversation flowing!
            """.trimIndent()
        )
    }

    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = MODEL_NAME,
            systemInstruction = systemPrompt
        )
    }

    fun streamReply(
        historyMessages: List<ChatMessageEntity>,
        userMessage: String
    ): Flow<MiaAiChunk> = flow {
        val recentHistory: List<Content> = historyMessages
            .takeLast(14)
            .map { msg ->
                val role = if (msg.sender == "user") "user" else "model"
                content(role = role) { text(msg.text) }
            }

        val chat = model.startChat(history = recentHistory)
        val accumulated = StringBuilder()

        try {
            withTimeout(60_000L) {
                chat.sendMessageStream(userMessage).collect { chunk ->
                    val piece = chunk.text.orEmpty()
                    if (piece.isNotEmpty()) {
                        accumulated.append(piece)
                        emit(MiaAiChunk.Partial(accumulated.toString()))
                    }
                }
            }
            val finalOutput = accumulated.toString().trim()
            if (finalOutput.isBlank()) {
                throw IllegalStateException("Mia returned an empty response. Please try again.")
            }
            emit(MiaAiChunk.Complete(finalOutput))
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Gemini streaming timed out", e)
            throw IllegalStateException("Connection timed out after 60s. Check your internet and try again.", e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Gemini streaming failed", e)
            throw e
        }
    }
}
