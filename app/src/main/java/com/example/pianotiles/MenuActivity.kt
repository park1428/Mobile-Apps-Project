package com.example.pianotiles

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val playButton = findViewById<Button>(R.id.playButton)
        val levelSelectButton = findViewById<Button>(R.id.levelSelectButton)

        playButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("LEVEL_NUMBER", 1)
            startActivity(intent)
        }

        levelSelectButton.setOnClickListener {
            val intent = Intent(this, LevelSelectActivity::class.java)
            startActivity(intent)
        }
    }
}