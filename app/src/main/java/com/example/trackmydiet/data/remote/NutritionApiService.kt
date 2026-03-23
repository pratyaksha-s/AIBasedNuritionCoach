package com.example.trackmydiet.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface NutritionApiService {

    @POST("v1beta/models/gemini-2.5-flash-lite:generateContent")
    suspend fun analyzeFood(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse

    @POST("v1beta/models/gemini-2.5-flash-lite:generateContent")
    suspend fun calculateGoals(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}