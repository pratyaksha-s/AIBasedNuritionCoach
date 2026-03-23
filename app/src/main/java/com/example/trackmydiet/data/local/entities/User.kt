package com.example.trackmydiet.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int = 1, // Single user app
    val name: String,
    val age: Int,
    val gender: String,
    val height: Float,
    val weight: Float,
    val activityLevel: String,
    val calorieTarget: Int,
    val proteinTarget: Int,
    val carbTarget: Int,
    val fatTarget: Int
)
