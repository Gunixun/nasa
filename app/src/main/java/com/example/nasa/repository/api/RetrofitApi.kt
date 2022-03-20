package com.example.nasa.repository.api

import com.example.nasa.repository.dto.MarsPicturesResponseData
import com.example.nasa.repository.dto.PictureByDayResponceData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    @GET("planetary/apod")
    fun getPictureByDay(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): Call<PictureByDayResponceData>

    @GET("/mars-photos/api/v1/rovers/curiosity/photos")
    fun getMarsPictureByDate(
        @Query("camera") camera: String = "fhaz",
        @Query("earth_date") earth_date: String,
        @Query("api_key") apiKey: String,
    ): Call<MarsPicturesResponseData>
}