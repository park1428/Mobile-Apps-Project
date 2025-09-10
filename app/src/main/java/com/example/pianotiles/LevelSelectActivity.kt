package com.example.pianotiles

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LevelSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)

        val recyclerView = findViewById<RecyclerView>(R.id.levelRecyclerView)
        val backButton = findViewById<Button>(R.id.backButton)

        // Vertical layout
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LevelAdapter(
            listOf(
                Level(1, "Easy"),
                Level(2, "Medium"),
                Level(3, "Hard"),
                Level(4, "Expert"),
                Level(1, "Easy"),
                Level(2, "Medium"),
                Level(3, "Hard"),
                Level(4, "Expert"),
                Level(1, "Easy"),
                Level(2, "Medium"),
                Level(3, "Hard"),
                Level(4, "Expert")
            )
        )

        // Back button finishes activity
        backButton.setOnClickListener { finish() }
    }
}
