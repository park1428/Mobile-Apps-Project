package com.example.pianotiles

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Random

class GameActivity : AppCompatActivity() {
    private lateinit var tileContainer: FrameLayout
    private lateinit var config: LevelConfig
    private lateinit var scoreText: TextView
    private lateinit var livesText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tileContainer = findViewById(R.id.tileContainer)
        scoreText = findViewById(R.id.scoreText)
        livesText = findViewById(R.id.livesText)

        config = initializeLevel()
        Player.score = 0

        tileContainer.post {
            startSpawningTiles()
            createLines()
        }
    }

    private fun createLines() {
        val columnWidth = tileContainer.width/4f
        for (i in 1..3) {
            val line = View(this).apply {
                layoutParams = FrameLayout.LayoutParams(3, FrameLayout.LayoutParams.MATCH_PARENT)
                setBackgroundColor(Color.LTGRAY)
                x = columnWidth * i
            }
            tileContainer.addView(line)
        }
    }

    private fun initializeLevel(): LevelConfig {
        val levelNumber = intent.getIntExtra("LEVEL_NUMBER", 1)
        val levelText = findViewById<TextView>(R.id.levelText)
        levelText.text = "Level $levelNumber"
        return LevelData.getLevelConfig(levelNumber)
    }

    private fun spawnTile() {
        val tileX = getRandomColumn()
        val tileWidth = tileContainer.width/4
        val tileHeight = (tileWidth * 1.5).toInt()

        val tile = Tile(size = tileWidth)
        // tile object is technically unnecessary right now
        // tiles will be added to buttons as tags and used to determine whether button interactions were correct or not
        // this is for later on when swiping is incorporated

        val tileButton = Button(this).apply {
            layoutParams = FrameLayout.LayoutParams(tileWidth, tileHeight)
            x = tileX
            y = -tileHeight.toFloat()
            setBackgroundColor(Color.BLACK)
            setOnClickListener {
                tileContainer.removeView(this)
                Player.score ++
                // $Player.score does not work
                // If it was $score, it would work
                scoreText.text = "Score: ${Player.score}"
            }
        }

        tileContainer.addView(tileButton)

        val duration = ((tileContainer.height + tile.size) / config.tileSpeed * 1000).toLong()
        // According to chatgpt's reasoning, height divided by speed = seconds, so this should give you the
        // exact time it takes until the tile goes off the screen
        tileButton.animate()
            .y(tileContainer.height.toFloat()) // move to this place
            .setDuration(duration) // move for this amount of time
            .withEndAction {
                tileContainer.removeView(tileButton)
            } // delete button when it ends moving
            .start()
    }

    private fun startSpawningTiles() {
        val handler = Handler(Looper.getMainLooper())
        val spawnRunnable = object : Runnable {
            override fun run() {
                val tilesToSpawn = (1..2).random()
                // ".." is inclusive. 1..4 does 1, 2, 3, 4
                // "until" is exclusive of the second parameter. 1 until 4 does 1, 2, 3
                for (i in 1..tilesToSpawn) {
                    spawnTile()
                }
                handler.postDelayed(this, config.spawnRate)
            }
        }
        handler.post(spawnRunnable)
    }

    private fun getRandomColumn(): Float {
        val columnWidth = tileContainer.width/4
        val column = (0 until 4).random()
        return (column * columnWidth).toFloat()
        // multiplying width by the column number selected gives you the x value of that column
    }
}