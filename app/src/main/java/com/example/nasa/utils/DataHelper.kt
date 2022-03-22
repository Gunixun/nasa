package com.example.nasa.utils

import com.example.nasa.model.MarsPictureModel
import com.example.nasa.model.PictureByDayModel
import com.example.nasa.repository.dto.MarsPicturesResponseData
import com.example.nasa.repository.dto.MarsServerResponseData
import com.example.nasa.repository.dto.PictureByDayResponceData

val BASEURL = "https://api.nasa.gov/"


fun convertPictureDtoToModel(responseData: PictureByDayResponceData):PictureByDayModel{
    return PictureByDayModel(
        date = responseData.date,
        explanation = responseData.explanation,
        hdurl = responseData.hdurl,
        mediaType = responseData.mediaType,
        serviceVersion = responseData.serviceVersion,
        title = responseData.title,
        url = responseData.url
    )
}

fun convertMarsPicturesDtoToModel(currentDate: String, responseData: MarsPicturesResponseData): MarsPictureModel{
    return if (!responseData.photos.isEmpty()){
        val marsResponse = responseData.photos.first()
        MarsPictureModel(
            date = marsResponse.earth_date.toString(),
            url = marsResponse.imgSrc
        )
    } else {
        MarsPictureModel(date = currentDate, url = null)
    }
}