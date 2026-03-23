package com.example.trackmydiet.data.repository

import com.example.trackmydiet.data.local.dao.WeightDao
import com.example.trackmydiet.data.local.entities.WeightEntry
import kotlinx.coroutines.flow.Flow

class WeightRepository(private val weightDao: WeightDao) {
    val allWeightEntries: Flow<List<WeightEntry>> = weightDao.getAllWeightEntries()

    suspend fun addWeightEntry(weight: Float) {
        weightDao.insertWeightEntry(WeightEntry(weight = weight, timestamp = System.currentTimeMillis()))
    }
}
