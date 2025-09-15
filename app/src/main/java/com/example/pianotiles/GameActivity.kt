package com.example.pianotiles

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Random

class GameActivity : AppCompatActivity() {
    private lateinit var tileContainer: FrameLayout
    private lateinit var config: LevelConfig
    private lateinit var scoreText: TextView
    private lateinit var livesText: TextView
//    private lateinit var handler : Handler
//    private lateinit var spawnRunnable : Runnable
    private var gameActive = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tileContainer = findViewById(R.id.tileContainer)
        scoreText = findViewById(R.id.scoreText)
        livesText = findViewById(R.id.livesText)
        livesText.text = "Lives: ${Player.lives}"

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

    private fun spawnTile(column: Int, sizeScale: Double) {
        val columnWidth = tileContainer.width/4
        val tileX = (column * columnWidth).toFloat()
        val tileWidth = tileContainer.width/4
        var tileHeight = (tileWidth * 1.5).toInt()
        tileHeight = (tileHeight * sizeScale).toInt()

        val tile = Tile(size = tileWidth)
        // tile object is technically unnecessary right now
        // tiles will be added to buttons as tags and used to determine whether button interactions were correct or not
        // this is for later on when swiping is incorporated

        val tileButton = Button(this).apply {
            tag = tile

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
                if (Player.score >= config.requiredPoints) {
                    endGame(true)
                }
            }
        }

        tileContainer.addView(tileButton)

        val duration = ((tileContainer.height + tile.size) / config.tileSpeed * 1000).toLong()
        // According to chatgpt's reasoning, height divided by speed = seconds, so this should give you the
        // exact time it takes until the tile goes off the screen
        tileButton.animate()
            .y(tileContainer.height.toFloat()) // move to this place
            .setDuration(duration) // move for this amount of time
            .setInterpolator(LinearInterpolator()) // figure out why this works
            .withEndAction {
                if (tileButton.parent != null) {
                    // A life should only be lost if the button still has a parent tileContainer
                    // This is because if you already clicked it should have been removed from
                    // the parent tileContainer
                    tileContainer.removeView(tileButton)
                    Player.lives --
                    val prefs = getSharedPreferences("gameprefs", MODE_PRIVATE)
                    prefs.edit().putInt("lives", Player.lives).apply()
                    if (Player.lives == 0) {
                        endGame(false)
                    }
                }
                livesText.text = "Lives: ${Player.lives}"
            } // delete button when it ends moving
            .start()
    }

    private fun startSpawningTiles() {
        // ".." is inclusive. 1..4 does 1, 2, 3, 4
        // "until" is exclusive of the second parameter. 1 until 4 does 1, 2, 3
        val handler = Handler(Looper.getMainLooper())
        val spawnRunnable = object : Runnable {
            override fun run() {
                if (!gameActive) {
                    return
                }
                val levelNumber = intent.getIntExtra("LEVEL_NUMBER", 1)
                var tilesToSpawn = 1
                if (levelNumber > 1) {
                    tilesToSpawn = (1..2).random()
                }
                if (levelNumber > 3) {
                    tilesToSpawn = (1..3).random()
                }
                val columns = (0 until 4).toMutableList()
                columns.shuffle()
                val chosenColumns = columns.take(tilesToSpawn)

                var sizeScale = 1.0
                if (levelNumber > 3) {
//                      val randomScale = 0.5 + Random().nextDouble() * 0.5
//                      tileHeight = (tileHeight * randomScale).toInt()
                    val isShorter = (1..4).random()
                    if (isShorter == 1) {
                        sizeScale = 0.7
                    }
                }

                for (column in chosenColumns) {
                    spawnTile(column, sizeScale)
                }
                handler.postDelayed(this, config.spawnRate)
            }
        }
        handler.post(spawnRunnable)
    }

    private fun endGame(win: Boolean) {
        gameActive = false

//        if (Player.score > Player.topScore) {
//            Player.topScore = Player.score
//        }

        for (i in 0 until tileContainer.childCount) {
            // goes through each items and stops the animation
            tileContainer.getChildAt(i).animate().cancel()
        }

        val winMessage = TextView(this).apply {
            text = "You won!"
            textSize = 40f
            setTextColor(Color.GREEN)
            setBackgroundColor(Color.WHITE)
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
            y -= 100f
        }
        val loseMessage = TextView(this).apply {
            text = "You lost!"
            textSize = 40f
            setTextColor(Color.RED)
            setBackgroundColor(Color.WHITE)
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
            y -= 100f
        }
        val menuButton = Button(this).apply {
            text = "Return to Menu"
            textSize = 40f
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
            y += 100f
            setOnClickListener {
                val intent = Intent(this@GameActivity, MenuActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        if (win) {
            tileContainer.addView(winMessage)
            winMessage.bringToFront()
        } else {
            tileContainer.addView(loseMessage)
            winMessage.bringToFront()
        }
        tileContainer.addView(menuButton)
    }

//    private fun getRandomColumn(): Float {
//        val columnWidth = tileContainer.width/4
//        val column = (0 until 4).random()
//        return (column * columnWidth).toFloat()
//        // multiplying width by the column number selected gives you the x value of that column
//    }
}