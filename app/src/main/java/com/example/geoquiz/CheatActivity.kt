package com.example.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.geoquiz.databinding.ActivityCheatBinding

// Using your package name as a qualifier for your extra,
// prevents name collisions with extras from other apps.
private const val EXTRA_ANSWER_IS_TRUE =
    "com.example.android.geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.example.android.geoquiz.answer_shown"

class CheatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheatBinding
    private var answerIsTrue = false
    private val viewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        binding.showAnswerButton.setOnClickListener {
            if (!viewModel.isCheated/*not cheat yet*/)
                cheat()
        }
        //binding.apiVersionTextView.text = Build.VERSION.SDK_INT.toString()
        showAnswer()
    }

    companion object{
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }


    private fun cheat(){
        viewModel.isCheated = true
        // make the current quiz as been cheated.
        viewModel.isCheated = true
        // show the answer of the current quiz.
        binding.answerTextView.setText(if (answerIsTrue) R.string.true_button else R.string.false_button)
        // make this quiz as been cheated.
        viewModel.isCheated = true
        // set the result to true (refers as cheater) that will back to main activity.
        setAnswerShownResult()
    }

    private fun setAnswerShownResult(){
            setResult(
                Activity.RESULT_OK,
                Intent().apply { putExtra(EXTRA_ANSWER_SHOWN, true) }
            )
    }

    private fun showAnswer(){
        /*
        this function calls on "onCreate" method.
        to save the state of the last value of the "isCheater" value.
        show the answer of the question if it is shown before.
        */
        if (viewModel.isCheated){
            binding.answerTextView.setText(if (answerIsTrue) R.string.true_button else R.string.false_button)
            setAnswerShownResult()
        }
    }

}