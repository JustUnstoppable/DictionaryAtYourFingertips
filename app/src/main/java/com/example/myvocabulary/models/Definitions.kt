package com.example.myvocabulary.models

import java.io.Serializable

data class Definitions(
    val definition:String,
    val synonyms:List<String>,
    val antonyms:List<String>,
    val example:String
):Serializable
