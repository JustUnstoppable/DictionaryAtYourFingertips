package com.example.myvocabulary.roomDB

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Saved_Vocabulary_Table")
data class SaveItemList(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val imageSave:Bitmap?,
    val dateSave:String?
)
