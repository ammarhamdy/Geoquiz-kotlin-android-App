package com.example.geoquiz

import android.app.Activity
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.geoquiz.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()
    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
            result ->
        // Handle the result
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheated =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            if (quizViewModel.isCheated){
                decreaseCheatLimit()
                binding.cheatButton.isEnabled = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.trueButton.setOnClickListener {
            checkAnswer(true)
            it.isEnabled = false
            binding.falseButton.isEnabled = false
        }
        binding.falseButton.setOnClickListener{
            checkAnswer(false)
            it.isEnabled = false
            binding.trueButton.isEnabled = false
        }
        binding.nextButton.setOnClickListener{
            quizViewModel.moveToNext()
            updateQuestion()
        }
        binding.previousButton.setOnClickListener{
            quizViewModel.moveToPrevious()
            updateQuestion()
        }
        binding.cheatButton.setOnClickListener {
            // Start CheatActivity
                cheatLauncher.launch(
                    CheatActivity.newIntent(
                        this@MainActivity,
                        quizViewModel.currentQuestionAnswer
                    )
                )
            }
        updateQuestion()
        setCheatLimit()
        // The Build.VERSION.SDK_INT constant contains the API level for the
        //version of Android used by the device.
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            blurCheatButton()
        */
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun blurCheatButton(){
        val effect = RenderEffect.createBlurEffect(
            10.0f,
            10.0f,
            Shader.TileMode.CLAMP
        )
        binding.cheatButton.setRenderEffect(effect)
    }

    private fun updateQuestion(){
        binding.questionTextView.setText(quizViewModel.currentQuestionText)
        binding.trueButton.isEnabled = true
        binding.falseButton.isEnabled = true
        binding.cheatButton.isEnabled = (!quizViewModel.isCheated) && (quizViewModel.cheatLimit > 0)
    }

    private fun checkAnswer(userAnswer: Boolean){
        var toastMessage = R.string.incorrect_toast
        if (quizViewModel.isCheated)
            toastMessage = R.string.judgment_toast
        else if (userAnswer == quizViewModel.currentQuestionAnswer)
            toastMessage = R.string.correct_toast
        Toast.makeText(
            this,
            toastMessage,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun decreaseCheatLimit(){
        quizViewModel.decreaseCheatLimit()
        binding.cheatLimitTextView.text = quizViewModel.cheatLimit.toString()
    }

    private fun setCheatLimit(){
        binding.cheatLimitTextView.text = quizViewModel.cheatLimit.toString()
        if (quizViewModel.cheatLimit == 0)
            binding.cheatButton.isEnabled = false
    }


}