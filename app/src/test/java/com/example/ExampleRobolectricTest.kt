package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Mia Talks", appName)
  }

  @Test
  fun `quiz questions have valid options and positive XP reward`() {
    assert(com.example.data.SampleData.XP_PER_CORRECT_QUIZ == 10)
    assert(com.example.data.SampleData.lessons.size >= 4)
    com.example.data.SampleData.quizQuestions.forEach { q ->
      assert(q.correctIndex in q.options.indices)
    }
  }
}
