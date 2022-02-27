package com.example.nasa.repository.api

import com.example.nasa.repository.dto.PictureDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PictureByDayApi {

    @GET("planetary/apod")
    fun getPictureByDay(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): Call<PictureDTO>
}