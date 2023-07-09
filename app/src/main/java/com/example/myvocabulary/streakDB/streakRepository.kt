package com.example.myvocabulary.streakDB

class streakRepository(private val streakDao: streakDao) {
    val streakList= streakDao.getAllStreaks()

    suspend fun insert(streak: streak):Long{
        return streakDao.addStreak(streak)
    }
}