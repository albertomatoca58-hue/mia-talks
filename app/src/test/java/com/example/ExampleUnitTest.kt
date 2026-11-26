package com.example

import com.example.data.SampleData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun sampleData_lessonsAndQuizzesAreValid() {
        assertEquals(10, SampleData.XP_PER_CORRECT_QUIZ)
        assertTrue(SampleData.lessons.size >= 4)
        assertTrue(SampleData.chatThreads.any { !it.isLiveAi })
        SampleData.quizQuestions.forEach { q ->
            assertTrue(q.correctIndex in q.options.indices)
            assertTrue(q.explanationPt.isNotBlank())
        }
    }
}
