package com.example.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.TimeZone

class MiaRepository(
    private val dao: MiaDao,
    private val context: Context
) {
    val userStats: Flow<UserStatsEntity> = dao.observeUserStats().map {
        it ?: UserStatsEntity()
    }

    val lessonProgress: Flow<Map<String, LessonProgressEntity>> =
        dao.observeLessonProgress().map { list ->
            list.associateBy { it.lessonId }
        }

    val recentDailyActivity: Flow<List<DailyActivityEntity>> =
        dao.observeRecentActivity(currentDayKey() - 6L)

    fun observeMessages(threadId: String): Flow<List<ChatMessageEntity>> =
        dao.observeMessages(threadId)

    suspend fun getMessagesOnce(threadId: String): List<ChatMessageEntity> =
        dao.getMessagesOnce(threadId)

    suspend fun ensureSeeded() {
        if (dao.countAllMessages() == 0) {
            val baseTime = System.currentTimeMillis() - 60_000L
            dao.insertMessages(SampleData.initialChatMessages(baseTime))
        }
        if (dao.getUserStatsOnce() == null) {
            dao.upsertUserStats(UserStatsEntity())
        }
    }

    suspend fun addMessage(message: ChatMessageEntity) {
        dao.insertMessage(message)
    }

    suspend fun clearLiveChat() {
        dao.clearLiveMessages(SampleData.THREAD_LIVE_AI)
        dao.insertMessage(
            SampleData.initialChatMessages(System.currentTimeMillis())
                .first { it.threadId == SampleData.THREAD_LIVE_AI }
        )
    }

    suspend fun completeLesson(lesson: LessonItem): Boolean {
        val existing = dao.getLessonProgress(lesson.id)
        if (existing?.isCompleted == true) {
            return false
        }
        dao.upsertLessonProgress(
            LessonProgressEntity(
                lessonId = lesson.id,
                isCompleted = true,
                completedAtMillis = System.currentTimeMillis(),
                xpEarned = lesson.xpReward
            )
        )
        recordActivity(xpDelta = lesson.xpReward, lessonDelta = 1, quizDelta = 0)
        return true
    }

    suspend fun awardQuizCorrectXp() {
        recordActivity(
            xpDelta = SampleData.XP_PER_CORRECT_QUIZ,
            lessonDelta = 0,
            quizDelta = 0,
            correctAnswerDelta = 1
        )
    }

    suspend fun recordQuizFinished() {
        val current = dao.getUserStatsOnce() ?: UserStatsEntity()
        dao.upsertUserStats(current.copy(quizzesCompleted = current.quizzesCompleted + 1))
        val today = currentDayKey()
        val daily = dao.getDailyActivityOnce(today) ?: DailyActivityEntity(dayKey = today)
        dao.upsertDailyActivity(daily.copy(quizzesDone = daily.quizzesDone + 1))
    }

    suspend fun awardPracticeChatXp() {
        recordActivity(xpDelta = SampleData.XP_PER_PRACTICE_CHAT, lessonDelta = 0, quizDelta = 0)
    }

    private suspend fun recordActivity(
        xpDelta: Int,
        lessonDelta: Int,
        quizDelta: Int,
        correctAnswerDelta: Int = 0
    ) {
        val today = currentDayKey()
        val current = dao.getUserStatsOnce() ?: UserStatsEntity()

        val newStreak = when {
            current.lastActiveDayKey == 0L -> 1
            current.lastActiveDayKey == today -> maxOf(1, current.currentStreakDays)
            current.lastActiveDayKey == today - 1L -> current.currentStreakDays + 1
            else -> 1
        }
        val bestStreak = maxOf(current.bestStreakDays, newStreak)

        dao.upsertUserStats(
            current.copy(
                totalXp = current.totalXp + xpDelta,
                currentStreakDays = newStreak,
                bestStreakDays = bestStreak,
                lastActiveDayKey = today,
                totalCorrectAnswers = current.totalCorrectAnswers + correctAnswerDelta
            )
        )

        val daily = dao.getDailyActivityOnce(today) ?: DailyActivityEntity(dayKey = today)
        dao.upsertDailyActivity(
            daily.copy(
                xpEarned = daily.xpEarned + xpDelta,
                lessonsDone = daily.lessonsDone + lessonDelta,
                quizzesDone = daily.quizzesDone + quizDelta
            )
        )
    }

    fun isOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    fun currentDayKey(): Long {
        val cal = Calendar.getInstance()
        val offsetMillis = TimeZone.getDefault().getOffset(cal.timeInMillis)
        return (cal.timeInMillis + offsetMillis) / 86_400_000L
    }
}
