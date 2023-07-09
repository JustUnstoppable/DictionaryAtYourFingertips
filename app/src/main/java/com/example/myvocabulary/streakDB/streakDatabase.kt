package com.example.myvocabulary.streakDB

import android.content.Context
import androidx.room.*

@Database(entities = [streak::class], version = 1, exportSchema = false)
@TypeConverters(ConverterClass::class)
abstract class streakDatabase :RoomDatabase(){
    abstract val streakDao:streakDao
    companion object{
        @Volatile
        private var INSTANCE:streakDatabase?=null
        fun getInstance(context: Context):streakDatabase{
            synchronized(this){
                var instance= INSTANCE
                if(instance==null){
                    instance= Room.databaseBuilder(
                        context.applicationContext,
                        streakDatabase::class.java,
                        "streak_database"
                    ).build()
                    INSTANCE=instance
                }
                return instance
            }
        }
    }
}