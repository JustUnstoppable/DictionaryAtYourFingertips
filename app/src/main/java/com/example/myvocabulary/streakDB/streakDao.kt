package com.example.myvocabulary.streakDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface streakDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStreak(streak: streak):Long

    @Query("SELECT * FROM streak_table")
    fun getAllStreaks():LiveData<List<streak>>
}