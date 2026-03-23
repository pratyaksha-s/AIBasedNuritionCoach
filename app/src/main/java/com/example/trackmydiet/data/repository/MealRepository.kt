package com.example.trackmydiet.data.repository

import com.example.trackmydiet.BuildConfig
import com.example.trackmydiet.data.local.dao.MealDao
import com.example.trackmydiet.data.local.entities.Meal
import com.example.trackmydiet.data.remote.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class MealRepository(
    private val mealDao: MealDao,
    private val apiService: NutritionApiService
) {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val nutritionAdapter = moshi.adapter(NutritionResponse::class.java)

    val allMeals: Flow<List<Meal>> = mealDao.getAllMeals()

    fun getMealsForDate(date: Long): Flow<List<Meal>> {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val endOfDay = calendar.timeInMillis
        return mealDao.getMealsForDay(startOfDay, endOfDay)
    }

    suspend fun analyzeAndSaveMeal(description: String?, imageBase64: String?): NutritionResponse {
        val prompt = """
            Analyze the following food ${if (description != null) "described as '$description'" else "in the image"}.
            Provide the estimated name, calories, protein (g), carbs (g), and fat (g).
            Respond ONLY with a valid JSON object in this format:
            {"name": "Food Name", "calories": 123, "protein": 10, "carbs": 20, "fat": 5}
        """.trimIndent()

        val parts = mutableListOf<Part>()
        parts.add(Part(text = prompt))
        if (imageBase64 != null) {
            parts.add(Part(inlineData = InlineData(mimeType = "image/jpeg", data = imageBase64)))
        }

        val request = GeminiRequest(
            contents = listOf(Content(parts = parts)),
            generationConfig = GenerationConfig(responseMimeType = "application/json")
        )
        val response = apiService.analyzeFood(BuildConfig.GEMINI_API_KEY, request)

        val resultText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: throw Exception("No response from AI")

        val cleanedJson = extractJson(resultText)
        
        val nutrition = nutritionAdapter.fromJson(cleanedJson)
            ?: throw Exception("Failed to parse nutrition data from: ${'$'}resultText")

        val meal = Meal(
            name = nutrition.name,
            calories = nutrition.calories,
            protein = nutrition.protein,
            carbs = nutrition.carbs,
            fat = nutrition.fat,
            timestamp = System.currentTimeMillis()
        )
        mealDao.insertMeal(meal)
        return nutrition
    }

    private fun extractJson(text: String): String {
        val startIndex = text.indexOf('{')
        val endIndex = text.lastIndexOf('}')
        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            return text.substring(startIndex, endIndex + 1)
        }
        return text.trim().removeSurrounding("```json", "```").trim()
    }

    suspend fun deleteMeal(mealId: Long) {
        mealDao.deleteMeal(mealId)
    }
}
