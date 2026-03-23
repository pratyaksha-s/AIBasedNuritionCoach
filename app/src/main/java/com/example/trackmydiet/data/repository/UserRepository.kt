package com.example.trackmydiet.data.repository

import com.example.trackmydiet.BuildConfig
import com.example.trackmydiet.data.local.dao.UserDao
import com.example.trackmydiet.data.local.entities.User
import com.example.trackmydiet.data.remote.*
import com.example.trackmydiet.domain.ActivityLevel
import com.example.trackmydiet.domain.calculator.CalorieCalculator
import com.example.trackmydiet.domain.calculator.UserProfile
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDao: UserDao,
//    private val apiService: NutritionApiService
) {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//    private val goalAdapter = moshi.adapter(GoalResponse::class.java)

    val user: Flow<User?> = userDao.getUser()

    suspend fun calculateAndSaveGoals(
        name: String,
        age: Int,
        gender: String,
        height: Float,
        weight: Float,
        activityLevel: ActivityLevel
    ) {
        val userProfile = UserProfile(name, weight, height, age, gender, activityLevel)
        val userPlan = CalorieCalculator.userSpecificPlan(userProfile)

       /* val prompt = """
            Based on the following user details:
            Age: ${age}, Gender: ${gender}, Height: ${height}cm, Weight: ${weight}kg, Activity Level: ${activityLevel}.
            Calculate the optimal daily calorie and macronutrient (protein, carbs, fat) targets.
            Respond ONLY with a valid JSON object in this format:
            {"calorieTarget": 2000, "proteinTarget": 150, "carbTarget": 200, "fatTarget": 60}
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            generationConfig = GenerationConfig(responseMimeType = "application/json")
        )
        val response = apiService.calculateGoals(BuildConfig.GEMINI_API_KEY, request)

        val resultText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: throw Exception("No response from AI")

        val cleanedJson = extractJson(resultText)
        val goals = goalAdapter.fromJson(cleanedJson)
            ?: throw Exception("Failed to parse goals data from: ${'$'}resultText")*/
        
        val user = User(
            name = name,
            age = age,
            gender = gender,
            height = height,
            weight = weight,
            activityLevel = activityLevel.name,
            calorieTarget = userPlan.calories,
            proteinTarget = userPlan.macro.protein,
            carbTarget = userPlan.macro.carbs,
            fatTarget = userPlan.macro.fats
        )
        userDao.insertUser(user)
    }

//    private fun extractJson(text: String): String {
//        val startIndex = text.indexOf('{')
//        val endIndex = text.lastIndexOf('}')
//        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
//            return text.substring(startIndex, endIndex + 1)
//        }
//        return text.trim().removeSurrounding("```json", "```").trim()
//    }
}
