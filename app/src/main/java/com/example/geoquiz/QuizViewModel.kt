package com.example.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.math.abs


const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
const val CHEAT_LIMIT_KEY = "CHEAT_LIMIT_KEY"
const val CHEAT_HISTORY_KEY = "CHEAT_HISTORY_KEY"

class QuizViewModel(
    private val savedStateHandle: SavedStateHandle
    ) : ViewModel(){

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    var cheatLimit: Int
        get() = savedStateHandle[CHEAT_LIMIT_KEY] ?: 3
        private set(value) = savedStateHandle.set(CHEAT_LIMIT_KEY, value)

    private var currentIndex : Int
        get() = savedStateHandle[CURRENT_INDEX_KEY] ?: 0
        set (value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    var isCheated: Boolean
        get() = quizHasBeenCheated()
        set(value) = markQuizAsCheated()

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious() {
        currentIndex = abs((currentIndex -1) % questionBank.size)
    }

    private fun markQuizAsCheated(){
        var cheatHistory: Array<Boolean>? = savedStateHandle[CHEAT_HISTORY_KEY]
        if (cheatHistory == null)
            cheatHistory = Array(questionBank.size) { false }
        cheatHistory[currentIndex] = true
        savedStateHandle[CHEAT_HISTORY_KEY] = cheatHistory
    }

    private fun quizHasBeenCheated(): Boolean{
        val cheatHistory: Array<Boolean>? = savedStateHandle[CHEAT_HISTORY_KEY]
        return if (cheatHistory == null){
            savedStateHandle[CHEAT_HISTORY_KEY] = Array(questionBank.size) { false }
            false
        } else
            cheatHistory[currentIndex]
    }

    fun decreaseCheatLimit() = cheatLimit--

}