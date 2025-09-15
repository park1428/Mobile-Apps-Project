package com.example.pianotiles

object LevelData {
     val levels = listOf(
        Level(1, "Level 1", "10 tiles to win"),
        Level(2, "Level 2", "10 tiles to win"),
        Level(3, "Level 3", "15 tiles to win"),
        Level(4, "Level 4", "20 tiles to win"),
        Level(5, "Level 5", "25 tiles to win")
    )
    fun getLevelConfig(level: Int): LevelConfig {
        return when(level) {
            1 -> LevelConfig(750f, 1500L, 10)
            2 -> LevelConfig(800f, 1400L, 15)
            3 -> LevelConfig(850f, 1350L, 15)
            4 -> LevelConfig(900f, 1300L, 20)
            5 -> LevelConfig(1000f, 1250L, 25)
            else -> LevelConfig(750f, 1500L, 10)
        }
    }
}