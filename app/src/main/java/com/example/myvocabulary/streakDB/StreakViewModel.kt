package com.example.myvocabulary.streakDB

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StreakViewModel(private val streakRepository: streakRepository):ViewModel() {
    var list=MutableLiveData<List<streak>>()

    val streakList=streakRepository.streakList

    fun insert(streak: streak) = viewModelScope.launch(Dispatchers.IO){
        val newRowId= streakRepository.insert(streak)
        withContext(Dispatchers.Main){
            if(newRowId> -1){
                Log.i("MYTAG","Screenshot Inserted Successfully ")
            }else{
                Log.i("MYTAG","Screenshot Failed ")
            }
        }

    }
}