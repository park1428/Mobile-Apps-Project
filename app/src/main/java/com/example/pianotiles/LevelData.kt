package com.example.pianotiles

object LevelData {
     val levels = listOf(
        Level(1, "Level 1", "20 tiles to win"),
        Level(2, "Level 2", "30 tiles to win"),
        Level(3, "Level 3", "30 tiles to win"),
        Level(4, "Level 4", "35 tiles to win"),
        Level(5, "Level 5", "40 tiles to win")
    )
    fun getLevelConfig(level: Int): LevelConfig {
        return when(level) {
            1 -> LevelConfig(700f, 1500L, 20)
            2 -> LevelConfig(800f, 1400L, 30)
            3 -> LevelConfig(850f, 1350L, 30)
            4 -> LevelConfig(900f, 1300L, 35)
            5 -> LevelConfig(1000f, 1250L, 40)
            else -> LevelConfig(700f, 1500L, 20)
        }
    }
}