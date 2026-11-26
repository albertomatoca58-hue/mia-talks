package com.example.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.MiaAiChunk
import com.example.ai.MiaAiService
import com.example.data.ChatMessageEntity
import com.example.data.DailyActivityEntity
import com.example.data.LessonItem
import com.example.data.MiaDatabase
import com.example.data.MiaRepository
import com.example.data.OfflinePracticeReply
import com.example.data.QuizQuestion
import com.example.data.SampleData
import com.example.data.UserStatsEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val TAG = "MiaViewModel"

sealed interface AiChatUiState {
    data object Idle : AiChatUiState
    data class Streaming(val partialText: String) : AiChatUiState
    data class Error(val message: String) : AiChatUiState
}

data class DayActivityBar(
    val dayLabel: String,
    val xpEarned: Int,
    val lessonsDone: Int,
    val isToday: Boolean
)

data class QuizUiState(
    val selectedCategory: String = "All Mix",
    val questions: List<QuizQuestion> = SampleData.quizQuestions,
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswered: Boolean = false,
    val sessionScore: Int = 0,
    val sessionXpEarned: Int = 0,
    val isFinished: Boolean = false
)

class MiaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MiaRepository
    private val aiService = MiaAiService()

    init {
        val dao = MiaDatabase.getInstance(application).miaDao()
        repository = MiaRepository(dao, application.applicationContext)
        viewModelScope.launch {
            repository.ensureSeeded()
        }
    }

    private val _activeThreadId = MutableStateFlow(SampleData.THREAD_LIVE_AI)
    val activeThreadId: StateFlow<String> = _activeThreadId.asStateFlow()

    private val _aiState = MutableStateFlow<AiChatUiState>(AiChatUiState.Idle)
    val aiState: StateFlow<AiChatUiState> = _aiState.asStateFlow()

    private val _usedPracticeReplyIds = MutableStateFlow<Set<String>>(emptySet())
    val usedPracticeReplyIds: StateFlow<Set<String>> = _usedPracticeReplyIds.asStateFlow()

    private val _selectedLessonId = MutableStateFlow<String?>(null)
    val selectedLessonId: StateFlow<String?> = _selectedLessonId.asStateFlow()

    private val _quizState = MutableStateFlow(QuizUiState())
    val quizState: StateFlow<QuizUiState> = _quizState.asStateFlow()

    private val _bannerMessage = MutableStateFlow<String?>(null)
    val bannerMessage: StateFlow<String?> = _bannerMessage.asStateFlow()

    val userStats: StateFlow<UserStatsEntity> = repository.userStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserStatsEntity())

    val completedLessonIds: StateFlow<Set<String>> = repository.lessonProgress
        .combine(MutableStateFlow(Unit)) { map, _ ->
            map.filterValues { it.isCompleted }.keys
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val weeklyActivity: StateFlow<List<DayActivityBar>> = repository.recentDailyActivity
        .combine(MutableStateFlow(Unit)) { list, _ ->
            buildLast7Days(list)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), buildLast7Days(emptyList()))

    @OptIn(ExperimentalCoroutinesApi::class)
    val activeMessages: StateFlow<List<ChatMessageEntity>> = _activeThreadId
        .flatMapLatest { threadId -> repository.observeMessages(threadId) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectThread(threadId: String) {
        _activeThreadId.value = threadId
        _aiState.value = AiChatUiState.Idle
    }

    fun isOnlineNow(): Boolean = repository.isOnline()

    fun sendLiveChatMessage(rawText: String) {
        val text = rawText.trim()
        if (text.isEmpty()) return
        if (_aiState.value is AiChatUiState.Streaming) return

        val threadId = _activeThreadId.value
        if (threadId != SampleData.THREAD_LIVE_AI) {
            _activeThreadId.value = SampleData.THREAD_LIVE_AI
        }

        viewModelScope.launch {
            val historyBefore = repository.getMessagesOnce(SampleData.THREAD_LIVE_AI)
            val userMsg = ChatMessageEntity(
                threadId = SampleData.THREAD_LIVE_AI,
                sender = "user",
                text = text,
                isSample = false
            )
            repository.addMessage(userMsg)

            if (!repository.isOnline()) {
                _aiState.value = AiChatUiState.Error(
                    "You appear to be offline. Live Mia AI needs an internet connection — tap any Sample Chat above to practice offline!"
                )
                return@launch
            }

            _aiState.value = AiChatUiState.Streaming("")
            try {
                var finalReply = ""
                aiService.streamReply(historyBefore, text).collect { chunk ->
                    when (chunk) {
                        is MiaAiChunk.Partial -> {
                            _aiState.value = AiChatUiState.Streaming(chunk.accumulatedText)
                        }
                        is MiaAiChunk.Complete -> {
                            finalReply = chunk.fullText
                        }
                    }
                }
                if (finalReply.isNotBlank()) {
                    repository.addMessage(
                        ChatMessageEntity(
                            threadId = SampleData.THREAD_LIVE_AI,
                            sender = "mia",
                            text = finalReply,
                            isSample = false
                        )
                    )
                    repository.awardPracticeChatXp()
                    _aiState.value = AiChatUiState.Idle
                } else {
                    _aiState.value = AiChatUiState.Error("Mia returned an empty response. Please try again.")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Live chat failed", e)
                _aiState.value = AiChatUiState.Error(
                    e.localizedMessage ?: "Could not reach Mia AI right now. Try again or use offline Sample Chats!"
                )
            }
        }
    }

    fun retryLastMessage() {
        viewModelScope.launch {
            val msgs = repository.getMessagesOnce(SampleData.THREAD_LIVE_AI)
            val lastUser = msgs.lastOrNull { it.sender == "user" }
            if (lastUser == null) {
                _aiState.value = AiChatUiState.Idle
                return@launch
            }
            if (!repository.isOnline()) {
                _aiState.value = AiChatUiState.Error(
                    "Still offline! Connect to the internet for Live Mia, or switch to Sample Chats above."
                )
                return@launch
            }
            val historyBefore = msgs.dropLastWhile { it.id == lastUser.id }
            _aiState.value = AiChatUiState.Streaming("")
            try {
                var finalReply = ""
                aiService.streamReply(historyBefore, lastUser.text).collect { chunk ->
                    when (chunk) {
                        is MiaAiChunk.Partial -> {
                            _aiState.value = AiChatUiState.Streaming(chunk.accumulatedText)
                        }
                        is MiaAiChunk.Complete -> {
                            finalReply = chunk.fullText
                        }
                    }
                }
                if (finalReply.isNotBlank()) {
                    repository.addMessage(
                        ChatMessageEntity(
                            threadId = SampleData.THREAD_LIVE_AI,
                            sender = "mia",
                            text = finalReply,
                            isSample = false
                        )
                    )
                    repository.awardPracticeChatXp()
                    _aiState.value = AiChatUiState.Idle
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Retry live chat failed", e)
                _aiState.value = AiChatUiState.Error(
                    e.localizedMessage ?: "Failed to reach Mia AI."
                )
            }
        }
    }

    fun dismissChatError() {
        _aiState.value = AiChatUiState.Idle
    }

    fun clearLiveChat() {
        viewModelScope.launch {
            repository.clearLiveChat()
            _aiState.value = AiChatUiState.Idle
        }
    }

    fun sendOfflinePracticeReply(reply: OfflinePracticeReply) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            repository.addMessage(
                ChatMessageEntity(
                    threadId = reply.threadId,
                    sender = "user",
                    text = reply.userPrompt,
                    timestampMillis = now,
                    isSample = true
                )
            )
            repository.addMessage(
                ChatMessageEntity(
                    threadId = reply.threadId,
                    sender = "mia",
                    text = reply.miaReply,
                    portugueseNote = reply.portugueseNote,
                    grammarTip = reply.grammarTip,
                    timestampMillis = now + 250L,
                    isSample = true
                )
            )
            if (!_usedPracticeReplyIds.value.contains(reply.id)) {
                _usedPracticeReplyIds.update { it + reply.id }
                repository.awardPracticeChatXp()
                _bannerMessage.value = "+${SampleData.XP_PER_PRACTICE_CHAT} XP for practicing with Mia! 🎉"
            }
        }
    }

    fun openLesson(lessonId: String?) {
        _selectedLessonId.value = lessonId
    }

    fun completeLesson(lesson: LessonItem) {
        viewModelScope.launch {
            val newlyCompleted = repository.completeLesson(lesson)
            _bannerMessage.value = if (newlyCompleted) {
                "Lesson '${lesson.title}' complete! +${lesson.xpReward} XP! 🔥"
            } else {
                "Awesome review of '${lesson.title}'! 🌟"
            }
        }
    }

    fun selectQuizCategory(category: String) {
        val filtered = if (category == "All Mix") {
            SampleData.quizQuestions
        } else {
            SampleData.quizQuestions.filter { it.category == category }
        }
        _quizState.value = QuizUiState(
            selectedCategory = category,
            questions = filtered
        )
    }

    fun submitQuizAnswer(optionIndex: Int) {
        val current = _quizState.value
        if (current.isAnswered || current.isFinished) return
        val question = current.questions.getOrNull(current.currentIndex) ?: return

        val isCorrect = optionIndex == question.correctIndex
        _quizState.update {
            it.copy(
                selectedOptionIndex = optionIndex,
                isAnswered = true,
                sessionScore = if (isCorrect) it.sessionScore + 1 else it.sessionScore,
                sessionXpEarned = if (isCorrect) {
                    it.sessionXpEarned + SampleData.XP_PER_CORRECT_QUIZ
                } else {
                    it.sessionXpEarned
                }
            )
        }
        if (isCorrect) {
            viewModelScope.launch {
                repository.awardQuizCorrectXp()
            }
        }
    }

    fun nextQuizQuestion() {
        val current = _quizState.value
        if (current.currentIndex + 1 >= current.questions.size) {
            _quizState.update { it.copy(isFinished = true) }
            viewModelScope.launch {
                repository.recordQuizFinished()
            }
        } else {
            _quizState.update {
                it.copy(
                    currentIndex = it.currentIndex + 1,
                    selectedOptionIndex = null,
                    isAnswered = false
                )
            }
        }
    }

    fun restartQuiz() {
        selectQuizCategory(_quizState.value.selectedCategory)
    }

    fun clearBannerMessage() {
        _bannerMessage.value = null
    }

    private fun buildLast7Days(records: List<DailyActivityEntity>): List<DayActivityBar> {
        val todayKey = repository.currentDayKey()
        val byDay = records.associateBy { it.dayKey }
        val cal = Calendar.getInstance()
        val fmt = SimpleDateFormat("EEE", Locale.getDefault())

        return (6 downTo 0).map { daysAgo ->
            val dayKey = todayKey - daysAgo
            val dayCal = (cal.clone() as Calendar).apply {
                add(Calendar.DAY_OF_YEAR, -daysAgo)
            }
            val label = fmt.format(dayCal.time).take(3)
            val entry = byDay[dayKey]
            DayActivityBar(
                dayLabel = label,
                xpEarned = entry?.xpEarned ?: 0,
                lessonsDone = entry?.lessonsDone ?: 0,
                isToday = daysAgo == 0
            )
        }
    }
}
