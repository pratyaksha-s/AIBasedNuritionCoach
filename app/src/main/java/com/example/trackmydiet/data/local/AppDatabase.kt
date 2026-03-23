package com.example.trackmydiet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trackmydiet.data.local.dao.MealDao
import com.example.trackmydiet.data.local.dao.UserDao
import com.example.trackmydiet.data.local.dao.WeightDao
import com.example.trackmydiet.data.local.entities.Meal
import com.example.trackmydiet.data.local.entities.User
import com.example.trackmydiet.data.local.entities.WeightEntry

@Database(entities = [User::class, Meal::class, WeightEntry::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mealDao(): MealDao
    abstract fun weightDao(): WeightDao
}
