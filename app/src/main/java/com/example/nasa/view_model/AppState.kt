package com.example.nasa.view_model

import com.example.nasa.model.MarsPictureModel
import com.example.nasa.model.PictureByDayData


sealed class AppState {
    data class SuccessPBD(val serverResponseData: PictureByDayData) : AppState()
    data class SuccessMarsPicture(val serverResponseData: MarsPictureModel) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class Loading(val progress: Int?) : AppState()
}