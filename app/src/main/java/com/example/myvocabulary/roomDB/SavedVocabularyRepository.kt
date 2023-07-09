package com.example.myvocabulary.roomDB

class SavedVocabularyRepository(private val dao:SavedVocabularyDao) {
    val savedLists = dao.getAllSaved()

    suspend fun insert(savedItemList: SaveItemList): Long {
        return dao.insertSaved(savedItemList)
    }

    suspend fun delete(savedItemList: SaveItemList): Int {
        return dao.deleteSaved(savedItemList)
    }

}