package com.maral.quizapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// Hauptbildschirm für das Quiz – zeigt Fragen, Optionen, Punkte und Timer an
class QuizActivity : AppCompatActivity() {

    private lateinit var questions: List<Question>
    private var currentQuestionIndex = 0
    private var score = 0
    private lateinit var countDownTimer: CountDownTimer
    private var timeLeft = 10000L

    private lateinit var tvQuestion: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvTimer: TextView
    private lateinit var btnOption1: Button
    private lateinit var btnOption2: Button
    private lateinit var btnOption3: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // UI-Elemente mit Layout verbinden
        tvQuestion = findViewById(R.id.tvQuestion)
        tvScore = findViewById(R.id.tvScore)
        tvTimer = findViewById(R.id.tvTimer)
        btnOption1 = findViewById(R.id.btnOption1)
        btnOption2 = findViewById(R.id.btnOption2)
        btnOption3 = findViewById(R.id.btnOption3)

        // Fragenliste definieren
        questions = listOf(
            Question("Was ist die Hauptstadt von Deutschland?", listOf("Berlin", "München", "Hamburg"), 0),
            Question("Wie viele Kontinente gibt es?", listOf("5", "6", "7"), 2),
            Question("Wer schrieb 'Faust'?", listOf("Schiller", "Goethe", "Kafka"), 1)
        )

        loadQuestion()
    }

    // Zeigt eine Frage an und initialisiert Buttons
    private fun loadQuestion() {
        if (currentQuestionIndex >= questions.size) {
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("score", score)
            intent.putExtra("total", questions.size)
            startActivity(intent)
            finish()
            return
        }

        val question = questions[currentQuestionIndex]
        tvQuestion.text = question.questionText
        tvScore.text = "Punkte: $score"

        val buttons = listOf(btnOption1, btnOption2, btnOption3)
        for (i in buttons.indices) {
            buttons[i].text = question.options[i]
            buttons[i].setBackgroundColor(Color.LTGRAY)
            buttons[i].isEnabled = true
        }

        startTimer()

        for (i in buttons.indices) {
            buttons[i].setOnClickListener {
                stopTimer()
                val correct = i == question.correctAnswerIndex
                if (correct) {
                    score++
                    buttons[i].setBackgroundColor(Color.GREEN)
                } else {
                    buttons[i].setBackgroundColor(Color.RED)
                }

                buttons.forEach { it.isEnabled = false }
                buttons[question.correctAnswerIndex].setBackgroundColor(Color.GREEN)

                btnOption1.postDelayed({
                    currentQuestionIndex++
                    loadQuestion()
                }, 1500)
            }
        }
    }

    // Startet den Countdown-Timer pro Frage
    private fun startTimer() {
        tvTimer.text = "Zeit: 10s"
        countDownTimer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                tvTimer.text = "Zeit: ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                tvTimer.text = "Zeit abgelaufen!"
                currentQuestionIndex++
                loadQuestion()
            }
        }.start()
    }

    private fun stopTimer() {
        countDownTimer.cancel()
    }

    // Sound deaktiviert – leerer Funktionskörper
    private fun playSound(resId: Int) {
        // nichts tun
    }
}

