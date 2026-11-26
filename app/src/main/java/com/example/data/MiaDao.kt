package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MiaDao {
    @Query("SELECT * FROM chat_messages WHERE threadId = :threadId ORDER BY timestampMillis ASC, id ASC")
    fun observeMessages(threadId: String): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages WHERE threadId = :threadId ORDER BY timestampMillis ASC, id ASC")
    suspend fun getMessagesOnce(threadId: String): List<ChatMessageEntity>

    @Query("SELECT COUNT(*) FROM chat_messages")
    suspend fun countAllMessages(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessageEntity>)

    @Query("DELETE FROM chat_messages WHERE threadId = :threadId AND isSample = 0")
    suspend fun clearLiveMessages(threadId: String)

    @Query("SELECT * FROM lesson_progress")
    fun observeLessonProgress(): Flow<List<LessonProgressEntity>>

    @Query("SELECT * FROM lesson_progress WHERE lessonId = :lessonId LIMIT 1")
    suspend fun getLessonProgress(lessonId: String): LessonProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLessonProgress(progress: LessonProgressEntity)

    @Query("SELECT * FROM user_stats WHERE id = 1")
    fun observeUserStats(): Flow<UserStatsEntity?>

    @Query("SELECT * FROM user_stats WHERE id = 1")
    suspend fun getUserStatsOnce(): UserStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserStats(stats: UserStatsEntity)

    @Query("SELECT * FROM daily_activity WHERE dayKey >= :minDayKey ORDER BY dayKey ASC")
    fun observeRecentActivity(minDayKey: Long): Flow<List<DailyActivityEntity>>

    @Query("SELECT * FROM daily_activity WHERE dayKey = :dayKey LIMIT 1")
    suspend fun getDailyActivityOnce(dayKey: Long): DailyActivityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDailyActivity(activity: DailyActivityEntity)
}
