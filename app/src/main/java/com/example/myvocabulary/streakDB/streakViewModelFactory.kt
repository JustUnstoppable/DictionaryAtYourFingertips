package com.example.myvocabulary.streakDB

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class streakViewModelFactory(private val streakRepository: streakRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StreakViewModel::class.java)){
            return StreakViewModel(streakRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}