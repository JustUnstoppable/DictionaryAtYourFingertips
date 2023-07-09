package com.example.myvocabulary.roomDB

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myvocabulary.roomDB.SaveItemList
import com.example.myvocabulary.roomDB.SavedVocabularyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedViewModel(private val repository: SavedVocabularyRepository) :ViewModel(){
    val savedLists=repository.savedLists
    private var isDelete=false
    private lateinit var SavedToDelete:SaveItemList

    val inputImg=MutableLiveData<Int>()
    val inputDate=MutableLiveData<String>()

    fun insert(saveItemList: SaveItemList) = viewModelScope.launch(Dispatchers.IO){
        val newRowId= repository.insert(saveItemList)
        withContext(Dispatchers.Main){
            if(newRowId> -1){
                Log.i("MYTAG","Screenshot Inserted Successfully ")
            }else{
                Log.i("MYTAG","Screenshot Failed ")
            }
        }
    }
    fun delete(saveItemList: SaveItemList)= viewModelScope.launch(Dispatchers.IO) {
        val rowsDeleted= repository.delete(saveItemList)
        withContext(Dispatchers.Main){
            if(rowsDeleted>0){
                inputImg.value=0
                inputDate.value=""

            }
        }
    }
}