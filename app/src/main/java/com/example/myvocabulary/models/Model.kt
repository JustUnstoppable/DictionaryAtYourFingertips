package com.example.myvocabulary.models

import java.io.Serializable

data class Model(
    val word: String?,
    val phonetic:String,
    val phonetics:List<Phonetics>,
    val meanings:List<Meanings>,
    val license: License,
    val sourceUrls:List<String>
):Serializable
