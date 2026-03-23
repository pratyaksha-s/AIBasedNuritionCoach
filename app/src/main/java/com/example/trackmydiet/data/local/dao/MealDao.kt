package com.example.trackmydiet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trackmydiet.data.local.entities.Meal
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Query("SELECT * FROM meals ORDER BY timestamp DESC")
    fun getAllMeals(): Flow<List<Meal>>

    @Query("SELECT * FROM meals WHERE timestamp >= :startOfDay AND timestamp < :endOfDay")
    fun getMealsForDay(startOfDay: Long, endOfDay: Long): Flow<List<Meal>>

    @Insert
    suspend fun insertMeal(meal: Meal)

    @Query("DELETE FROM meals WHERE id = :mealId")
    suspend fun deleteMeal(mealId: Long)
}
