package com.example.myvocabulary.streakDB

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*
@Entity(tableName = "streak_table")
data class streak(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val streakDate: String
)
