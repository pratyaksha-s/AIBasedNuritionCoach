package com.example.trackmydiet

import android.app.Application
import androidx.room.Room
import com.example.trackmydiet.data.local.AppDatabase
import com.example.trackmydiet.data.remote.NutritionApiService
import com.example.trackmydiet.data.repository.MealRepository
import com.example.trackmydiet.data.repository.UserRepository
import com.example.trackmydiet.data.repository.WeightRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TrackMyDietApplication : Application() {

    lateinit var database: AppDatabase
    lateinit var userRepository: UserRepository
    lateinit var mealRepository: MealRepository
    lateinit var weightRepository: WeightRepository
    lateinit var apiService: NutritionApiService

    override fun onCreate() {
        super.onCreate()
        
        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "trackmydiet_db"
        ).fallbackToDestructiveMigration().build()

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/") 
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        apiService = retrofit.create(NutritionApiService::class.java)
        userRepository = UserRepository(database.userDao())
        mealRepository = MealRepository(database.mealDao(), apiService)
        weightRepository = WeightRepository(database.weightDao())
    }
}
