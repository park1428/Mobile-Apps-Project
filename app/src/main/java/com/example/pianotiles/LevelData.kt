package com.example.pianotiles

object LevelData {
     val levels = listOf(
        Level(1, "Level 1", "description 1"),
        Level(2, "Level 2", "description 2"),
        Level(3, "Level 3", "description 3"),
        Level(4, "Level 4", "description 4"),
        Level(5, "Level 5", "description 5")
    )
    fun getLevelConfig(level: Int): LevelConfig {
        return when(level) {
            1 -> LevelConfig(700f, 1500L)
            2 -> LevelConfig(800f, 1400L)
            3 -> LevelConfig(850f, 1350L)
            4 -> LevelConfig(900f, 1300L)
            5 -> LevelConfig(1000f, 1250L)
            else -> LevelConfig(700f, 1500L)
        }
    }
}