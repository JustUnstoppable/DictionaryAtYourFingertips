package com.example.myvocabulary.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SaveItemList::class],version=1)
@TypeConverters(Converters::class)
abstract class SavedVocabularyDatabase :RoomDatabase(){
    abstract val savedVocabularyDao:SavedVocabularyDao

    companion object{
        @Volatile
        private var INSTANCE:SavedVocabularyDatabase?=null
        fun getInstance(context: Context):SavedVocabularyDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance==null){
                    instance=Room.databaseBuilder(
                        context.applicationContext,
                        SavedVocabularyDatabase::class.java,
                        "saved_vocabulary_database"
                    ).build()
                    INSTANCE=instance
                }
                return instance
            }
        }
    }
}