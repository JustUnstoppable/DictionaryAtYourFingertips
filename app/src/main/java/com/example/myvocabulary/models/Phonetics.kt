package com.example.myvocabulary.models

import java.io.Serializable
import java.net.URL

data class Phonetics(
    val text:String,
    val audio:String,
    val sourceUrl:String,
    val licence: Licence
):Serializable
