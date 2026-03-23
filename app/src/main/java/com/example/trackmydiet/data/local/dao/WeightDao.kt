package com.example.trackmydiet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trackmydiet.data.local.entities.WeightEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightDao {
    @Query("SELECT * FROM weight_entries ORDER BY timestamp ASC")
    fun getAllWeightEntries(): Flow<List<WeightEntry>>

    @Insert
    suspend fun insertWeightEntry(entry: WeightEntry)
}
