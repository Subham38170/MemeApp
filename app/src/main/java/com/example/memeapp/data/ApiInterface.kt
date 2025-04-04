package com.example.memeapp.data

import com.example.memeapp.models.AllMemesData
import com.example.memeapp.models.Meme
import com.example.memeapp.models.MemesList
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("get_memes")
    suspend fun getMemesList(): Response<AllMemesData>
}