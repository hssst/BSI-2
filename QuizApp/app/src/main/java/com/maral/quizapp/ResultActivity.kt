package com.maral.quizapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// Zeigt die Endauswertung an – Punktzahl und Highscore
class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 0)

        val prefs = getSharedPreferences("quiz_prefs", Context.MODE_PRIVATE)
        val highscore = prefs.getInt("highscore", 0)

        val tvResult = findViewById<TextView>(R.id.tvResult)
        val tvHighscore = findViewById<TextView>(R.id.tvHighscore)

        tvResult.text = "Du hast $score von $total richtig!"

        if (score > highscore) {
            prefs.edit().putInt("highscore", score).apply()
            tvHighscore.text = "🎉 Neuer Highscore: $score 🎉"
        } else {
            tvHighscore.text = "Highscore: $highscore"
        }

        val btnRestart = findViewById<Button>(R.id.btnRestart)
        btnRestart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}

