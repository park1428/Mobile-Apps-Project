package com.example.pianotiles

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.timer

class MenuActivity : AppCompatActivity() {
    private lateinit var livesText: TextView
    private lateinit var playButton: Button
    private lateinit var levelSelectButton: Button
    private lateinit var container : LinearLayout
    private lateinit var timerText : TextView
//    private lateinit var topScoreText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
//        val prefs = getSharedPreferences("gameprefs", MODE_PRIVATE)
//        prefs.edit()
//            .putInt("lives", 5)
//            .putLong("livesResetTime", 0L)
//            .apply()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
//        topScoreText = findViewById(R.id.topScoreText)
        livesText = findViewById(R.id.livesText)

        playButton = findViewById<Button>(R.id.playButton)
        levelSelectButton = findViewById<Button>(R.id.levelSelectButton)
        container = findViewById<LinearLayout>(R.id.container)

    playButton.setOnClickListener {
            val levelNumber = intent.getIntExtra("LEVEL_NUMBER", 1)
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("LEVEL_NUMBER", levelNumber)
            startActivity(intent)
        }

        levelSelectButton.setOnClickListener {
            val intent = Intent(this, LevelSelectActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
//        topScoreText.text = "Top Score: ${Player.topScore}"
        val prefs = getSharedPreferences("gameprefs", MODE_PRIVATE)
        Player.lives = prefs.getInt("lives", 5)
        livesText.text = "Lives: ${Player.lives}"

        timerText = TextView(this).apply {
            text = "Time Left: "
            textSize = 40f
            setTextColor(Color.WHITE)
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
//            y -= 400f
        }

        if (Player.lives == 0) {
            playButton.isEnabled = false
            playButton.alpha = .5f
            if (timerText.parent == null) {
                container.addView(timerText)
            }

            val prefs = getSharedPreferences("gameprefs", MODE_PRIVATE)
            var resetTime = prefs.getLong("livesResetTime", 0L)
            val currentTime = System.currentTimeMillis()
            if (resetTime == 0L) {
                resetTime = currentTime + 1000 * 10
                prefs.edit().putLong("livesResetTime", resetTime).apply()
            }

            if (currentTime < resetTime) {
                val timeLeft = resetTime - currentTime
                val timer = object : CountDownTimer(timeLeft, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val minutes = (millisUntilFinished / 1000) / 60
                        val seconds = (millisUntilFinished / 1000) % 60
                        timerText.text = "Time Left: %02d:%02d".format(minutes, seconds)
                    }
                    override fun onFinish() {
                        Player.lives = 5
                        val prefs = getSharedPreferences("gameprefs", MODE_PRIVATE)
                        prefs.edit().putInt("lives", Player.lives).apply()
                        livesText.text = "Lives: ${Player.lives}"
                        prefs.edit().putLong("livesResetTime", 0L).apply() // for next time

                        playButton.isEnabled = true
                        playButton.alpha = 1f
                        container.removeView(timerText)
                    }
                }
                timer.start()
            }
        }

//        if (Player.lives == 0 && currentTime < resetTime) {
//            playButton.isEnabled = false
//            playButton.alpha = .5f
//            container.addView(timerText)
//        }
    }
}