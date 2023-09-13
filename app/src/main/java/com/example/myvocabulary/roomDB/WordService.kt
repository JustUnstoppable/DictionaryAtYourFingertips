package com.example.myvocabulary

import com.example.myvocabulary.models.Model
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WordService {
    @GET("api/v2/entries/en/{word}")
    fun meaning(@Path("word") word: String): Call<List<Model>>
}
