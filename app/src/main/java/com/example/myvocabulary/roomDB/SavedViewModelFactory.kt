package com.example.myvocabulary.roomDB

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SavedViewModelFactory(private val repository:SavedVocabularyRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SavedViewModel::class.java)){
            return SavedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}