package com.example.nasa.view_model

import com.example.nasa.model.PictureModel


sealed class PictureByDayState {
    data class Success(val serverResponseData: PictureModel) : PictureByDayState()
    data class Error(val error: Throwable) : PictureByDayState()
    data class Loading(val progress: Int?) : PictureByDayState()
}