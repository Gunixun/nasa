package com.example.nasa.repository

import com.example.nasa.BuildConfig
import com.example.nasa.model.PictureByDayData
import com.example.nasa.repository.api.RetrofitApi
import com.example.nasa.repository.dto.PictureByDayResponceData
import com.example.nasa.utils.BASEURL
import com.example.nasa.utils.CallbackData
import com.example.nasa.utils.convertPictureDtoToModel
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class PictureRetrofitRepositoryImpl : IPictureRepository {

    private val api by lazy {
        Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build().create(RetrofitApi::class.java)
    }

    override fun getPictureByDate(date: Date, callback: CallbackData<PictureByDayData>) {
        val sdf = SimpleDateFormat("yyyy-M-dd")
        val currentDate = sdf.format(date)
        api.getPictureByDay(BuildConfig.NASA_API_KEY, currentDate).enqueue(
            object : Callback<PictureByDayResponceData> {
                override fun onResponse(
                    call: Call<PictureByDayResponceData>,
                    response: Response<PictureByDayResponceData>
                ) {
                    val pictureByDayResponceData: PictureByDayResponceData? = response.body()
                    if (pictureByDayResponceData != null) {
                        callback.onSuccess(
                            convertPictureDtoToModel(pictureByDayResponceData)
                        )
                    } else {
                        callback.onError(Throwable())
                    }
                }

                override fun onFailure(call: Call<PictureByDayResponceData>, e: Throwable) {
                    callback.onError(e)
                }
            }
        )
    }
}