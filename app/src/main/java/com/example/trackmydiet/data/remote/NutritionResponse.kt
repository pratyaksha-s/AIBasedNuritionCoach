package com.example.trackmydiet.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NutritionResponse(
    @Json(name = "name") val name: String,
    @Json(name = "calories") val calories: Int,
    @Json(name = "protein") val protein: Int,
    @Json(name = "carbs") val carbs: Int,
    @Json(name = "fat") val fat: Int
)

@JsonClass(generateAdapter = true)
data class GoalResponse(
    @Json(name = "calorieTarget") val calorieTarget: Int,
    @Json(name = "proteinTarget") val proteinTarget: Int,
    @Json(name = "carbTarget") val carbTarget: Int,
    @Json(name = "fatTarget") val fatTarget: Int
)
