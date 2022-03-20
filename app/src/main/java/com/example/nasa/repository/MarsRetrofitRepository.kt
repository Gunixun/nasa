package com.example.nasa.repository

import com.example.nasa.BuildConfig
import com.example.nasa.model.MarsPictureModel
import com.example.nasa.repository.api.RetrofitApi
import com.example.nasa.repository.dto.MarsPicturesResponseData
import com.example.nasa.utils.CallbackData
import com.example.nasa.utils.convertMarsPicturesDtoToModel
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MarsRetrofitRepository : IMarsPictureRepository {

    private val baseUrl = "https://api.nasa.gov/"
    private val api by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build().create(RetrofitApi::class.java)
    }

    override fun getMarsPicture(date: Date, callback: CallbackData<MarsPictureModel>) {
        val sdf = SimpleDateFormat("yyyy-M-dd")
        val currentDate = sdf.format(date)
        api.getMarsPictureByDate(currentDate, BuildConfig.NASA_API_KEY).enqueue(
            object : Callback<MarsPicturesResponseData> {
                override fun onResponse(call: Call<MarsPicturesResponseData>, response: Response<MarsPicturesResponseData>) {
                    val marsPicturesResponseData: MarsPicturesResponseData? = response.body()
                    if (marsPicturesResponseData != null) {
                        callback.onSuccess(
                            convertMarsPicturesDtoToModel(currentDate, marsPicturesResponseData)
                        )
                    } else {
                        callback.onError(Throwable())
                    }
                }

                override fun onFailure(call: Call<MarsPicturesResponseData>, e: Throwable) {
                    callback.onError(e)
                }
            }
        )
    }
}