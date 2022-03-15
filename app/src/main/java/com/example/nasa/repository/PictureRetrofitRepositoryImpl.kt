package com.example.nasa.repository


import com.example.nasa.BuildConfig
import com.example.nasa.model.PictureModel
import com.example.nasa.repository.api.PictureByDayApi
import com.example.nasa.repository.dto.PictureDTO
import com.example.nasa.utils.CallbackData
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class PictureRetrofitRepositoryImpl : IPictureRepository {

    private val baseUrl = "https://api.nasa.gov/"
    private val api by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build().create(PictureByDayApi::class.java)
    }

    override fun getPictureByDate(date: Date, callback: CallbackData<PictureModel>){
        val sdf = SimpleDateFormat("yyyy-M-dd")
        val currentDate = sdf.format(date)
        api.getPictureByDay(BuildConfig.NASA_API_KEY, currentDate).enqueue(
            object : Callback<PictureDTO> {
                override fun onResponse(call: Call<PictureDTO>, response: Response<PictureDTO>) {
                    val pictureDTO: PictureDTO? = response.body()
                    if (pictureDTO != null) {
                        callback.onSuccess(
                            PictureModel(
                                date = pictureDTO.date,
                                explanation = pictureDTO.explanation,
                                hdurl = pictureDTO.hdurl,
                                mediaType = pictureDTO.mediaType,
                                serviceVersion = pictureDTO.serviceVersion,
                                title = pictureDTO.title,
                                url = pictureDTO.url
                            )
                        )
                    } else {
                        callback.onError(Throwable())
                    }
                }

                override fun onFailure(call: Call<PictureDTO>, e: Throwable) {
                    callback.onError(e)
                }
            }
        )
    }
}