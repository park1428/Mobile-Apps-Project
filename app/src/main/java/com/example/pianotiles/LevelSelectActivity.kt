package com.example.pianotiles

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

class LevelSelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)

        val levelGrid = findViewById<GridLayout>(R.id.levelGrid)
        val totalLevels = 5

        for (level in LevelData.levels) {
            val buttonText = level.name
            val descriptionText = level.description
            val levelNumber = level.number

            val levelButton = Button(this).apply{
                text = buttonText
                textSize = 20f
                minHeight = 150
                minWidth = 300
                setPadding(20, 20, 20, 20)
            }
            levelButton.setOnClickListener {
                val intent = Intent(this@LevelSelectActivity, GameActivity::class.java)
                intent.putExtra("LEVEL_NUMBER", levelNumber)
                startActivity(intent)
            }

            val description = TextView(this).apply {
                text = descriptionText
                textSize = 20f
                gravity = Gravity.CENTER_VERTICAL
            }

            levelGrid.addView(levelButton)
            levelGrid.addView(description)
        }

//        for (i in 1..totalLevels) {
//            val levelButton = Button(this)
//            levelButton.text = "Level $i"
//
//            levelButton.setOnClickListener {
//                val intent = Intent(this, GameActivity::class.java)
//                intent.putExtra("LEVEL_NUMBER", i)
//                startActivity(intent)
//            }
//            levelGrid.addView(levelButton)
//        }
    }
}