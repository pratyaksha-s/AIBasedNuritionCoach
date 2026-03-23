package com.example.trackmydiet.domain

enum class ActivityLevel(
    val description: String,
    val multiplier: Double
) {
    SEDENTARY("Sedentary (little or no exercise)", 1.2),
    LIGHT("Light activity (light exercise/sports 1-3 days/week)", 1.375),
    MODERATE("Moderate activity (moderate exercise/sports 3-5 days/week)", 1.55),
    ACTIVE("Active (hard exercise/sports 6-7 days/week)", 1.725),
    VERY_ACTIVE("Very active (very hard exercise/sports & physical job)", 1.9)
}