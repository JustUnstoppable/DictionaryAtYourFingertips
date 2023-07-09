package com.example.myvocabulary.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SavedVocabularyDao {
    @Insert
    suspend fun insertSaved(savedItemList: SaveItemList):Long

    @Delete
    suspend fun deleteSaved(savedItemList: SaveItemList):Int

    @Query("SELECT * FROM saved_vocabulary_table")
    fun getAllSaved():LiveData<List<SaveItemList>>
}