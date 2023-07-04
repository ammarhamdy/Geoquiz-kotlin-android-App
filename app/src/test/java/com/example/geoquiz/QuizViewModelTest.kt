package com.example.geoquiz

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.*
import org.junit.Test


internal class QuizViewModelTest{

    @Test
    fun providesExpectedQuestionText(){
        /*the first question text must be a question about australia*/
        val quizViewModel = QuizViewModel(SavedStateHandle())
        assertEquals(
            R.string.question_australia,
            quizViewModel.currentQuestionText
        )
    }

    @Test
    fun wrapsAroundQuestionBank(){
        val quizViewModel = QuizViewModel(
            SavedStateHandle(mapOf(CURRENT_INDEX_KEY to 5)
            )
        )
        assertEquals(
            R.string.question_asia,
            quizViewModel.currentQuestionText
        )
        quizViewModel.moveToNext()
        assertEquals(
            R.string.question_australia,
            quizViewModel.currentQuestionText
        )
    }

}