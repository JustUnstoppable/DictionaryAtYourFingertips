package com.example.myvocabulary.models

import java.io.Serializable

data class Meanings(
    val partOfSpeech:String,
    val definitions: List<Definitions>,
    val synonyms:List<String>,
    val antonyms:List<String>
):Serializable
