package com.example.trackmydiet.domain.calculator

import android.R.attr.height
import androidx.compose.ui.text.font.FontVariation.weight
import com.example.trackmydiet.data.local.entities.User
import com.example.trackmydiet.domain.ActivityLevel

object CalorieCalculator {

    fun userSpecificPlan(userProfile: UserProfile) : UserPlan {
        val bmr = calculateBMR(userProfile.weight, userProfile.height, userProfile.age)
        val tdee = calculateTDEE(bmr, userProfile.activity)
        val calories = fatLossCalories(tdee)
        return UserPlan(
            calories = calories,
            macro = macros(calories)
        )
    }

    fun calculateBMR(weight: Float, height: Float, age: Int): Double {
        return 10 * weight + 6.25 * height - 5 * age + 5
    }

    fun calculateTDEE(bmr: Double, activityLevel: ActivityLevel): Double {
        return bmr * activityLevel.multiplier
    }

    fun fatLossCalories(tdee: Double): Int {
        return (tdee - 500).toInt()
    }

    fun macros(calories: Int): Macro {
        val protein = (calories * 0.3) / 4
        val carbs = (calories * 0.4) / 4
        val fats = (calories * 0.3) / 9

        return Macro(
            protein = protein.toInt(),
            carbs = carbs.toInt(),
            fats = fats.toInt()
        )
    }
}


