package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val threadId: String,
    val sender: String, // "mia" or "user"
    val text: String,
    val portugueseNote: String? = null,
    val grammarTip: String? = null,
    val timestampMillis: Long = System.currentTimeMillis(),
    val isSample: Boolean = false
)

@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
    @PrimaryKey val lessonId: String,
    val isCompleted: Boolean = false,
    val completedAtMillis: Long? = null,
    val xpEarned: Int = 0
)

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey val id: Int = 1,
    val totalXp: Int = 0,
    val currentStreakDays: Int = 0,
    val bestStreakDays: Int = 0,
    val lastActiveDayKey: Long = 0L,
    val quizzesCompleted: Int = 0,
    val totalCorrectAnswers: Int = 0
)

@Entity(tableName = "daily_activity")
data class DailyActivityEntity(
    @PrimaryKey val dayKey: Long,
    val xpEarned: Int = 0,
    val lessonsDone: Int = 0,
    val quizzesDone: Int = 0
)
